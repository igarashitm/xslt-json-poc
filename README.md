# XSLT JSON data mapping PoC
Exercise XSLT JSON mappings manually to find a way for Kaoto DataMapper JSON support.

## With XSLT 3.0 built-in functions
Use `json-to-xml()` and `xml-to-json()` to perform JSON mappings.

### [JUnit Test to run following 3 cases](src/test/java/com/github/igarashitm/xsltjsonpoc/WithBuiltinTest.java)

### Converts JSON input to the XML representation
- [Camel Route](src/test/resources/01-with-builtin/01-01-source.yaml)
- [XSLT file](src/test/resources/01-with-builtin/01-01-source.xsl)

Simulates delivering a JSON body to the Kaoto DataMapper step. Since camel-xslt-saxon assumes the body
to be XML Document - if the main input is not XML, XSLT processor throws an error - in order to avoid
an error, it
- relocates the Camel message body to a variable (`kaotoDataMapperBody`)
- sets `null` to the message body
- sets `failOnNullBody` to `false` on camel-xslt-saxon endpoint

and then uses the relocated variable as a parameter inside the XSLT.

`json-to-xml()` function converts JSON into an intermediate XML format, so called the lossless conversion.
In that intermediate format, JSON array is represented with `array` element, JSON object is represented
with `map` element, a string text node is represented with `string` element, and so on. Here is an example:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<array xmlns="http://www.w3.org/2005/xpath-functions">
   <map>
      <string key="title">Apple</string>
      <string key="Note">Fuji</string>
      <number key="Quantity">10</number>
      <number key="Price">5.00</number>
   </map>
   <map>
      <string key="title">Banana</string>
      <string key="Note">Philippines</string>
      <number key="Quantity">5</number>
      <number key="Price">16.05</number>
   </map>
</array>
```
In this XSLT experiment, it performs an additional conversion to the logical XML document structure, so that
the document tree rendered in DataMapper UI would be more human friendly. Here is the output from the JUnit test:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<array>
    <map>
        <Title>Apple</Title>
        <Note>Fuji</Note>
        <Quantity>10</Quantity>
        <Price>5.00</Price>
    </map>
    <map>
        <Title>Banana</Title>
        <Note>Philippines</Note>
        <Quantity>5</Quantity>
        <Price>16.05</Price>
    </map>
</array>
```
While it still uses `array` element for an anonymous array and `map` for an anonymous object,
it creates an element with the `key` as a name where it's available. In this way,
DataMapper can generate cleaner and easy to read XPath expression as a mapping outcome, for example:
- lossless: `/array/map/string[@key='Title']`
- logical: `/array/map/Title`

### Creates JSON output
- [Camel Route](src/test/resources/01-with-builtin/01-02-target.yaml)
- [XSLT file](src/test/resources/01-with-builtin/01-02-target.xsl)

Simulates creating a JSON output out of the Kaoto DataMapper step. Deliver `Cart` XML in the body,
`Account` XML in the `account` variable, and a sequence number in the `orderSequence` variable,
then create a `ShipOrder` JSON object out of them.
If you look into the 
[XSLT file](src/test/resources/01-with-builtin/01-02-target.xsl), you can see the lossless
elements are directly placed instead of the logical format of the target document. While DataMapper
UI still renders the tree representation of logical target document, it is expected that the
Kaoto DataMapper serializer/deserializer handles lossless format and converts from/to the
Kaoto DataMapper internal mapping model objects.

Here is the output from the JUnit test:
```json
{
  "OrderId" : "ORD-ACC001-263",
  "OrderPerson" : "acc001 : Tarou",
  "ShipTo" : {
    "Name" : "Tarou",
    "Address" : {
      "Street" : "314 Littleton Rd",
      "City" : "Westford",
      "State" : "",
      "Country" : "US"
    }
  },
  "Item" : [ {
    "Title" : "Apple",
    "Quantity" : 10,
    "Price" : 5
  }, {
    "Title" : "Banana",
    "Quantity" : 5,
    "Price" : 16.05
  } ]
}
```
### Creates JSON output out of multiple JSON inputs
- [Camel Route](src/test/resources/01-with-builtin/01-03-full.yaml)
- [XSLT file](src/test/resources/01-with-builtin/01-03-full.xsl)

With combining JSON inputs and JSON output experiments above, this shows the XSLT reference implementation
for the DataMapper JSON support. Deliver `Cart` JSON object in the body, `Account` JSON object
in the `account` variable, and a sequence number in the `orderSequence` variable, then create
a `ShipOrder` JSON object out of them.

This doesn't only mean JSON to JSON mapping is achieved, but it also means that any of inputs and
an output could be JSON format in a single DataMapper step, which means we can achieve cross
format data mappings between XML and JSON.

Here is the output from the JUnit test:
```json
{
  "OrderId" : "ORD-ACC001-534",
  "OrderPerson" : "acc001 : Tarou",
  "ShipTo" : {
    "Name" : "Tarou",
    "Address" : {
      "Street" : "314 Littleton Rd",
      "City" : "Westford",
      "State" : "Massachusetts",
      "Country" : "US"
    }
  },
  "Item" : [ {
    "Title" : "Apple",
    "Quantity" : 10,
    "Price" : 5
  }, {
    "Title" : "Banana",
    "Quantity" : 5,
    "Price" : 16.05
  } ]
}
```
Lastly, check again how 2-step conversion cleans up the XPath expression compared to the lossless structure: 
- lossless: `$jsonAccount/map/map[@key='Address']/string[@key='Street']`
- logical: `$jsonAccount/map/Address/Street`

It will be much intuitive when the document structure is rendered as a tree structure on DataMapper UI.

## With camel-xj
Experimentation on hold. While camel-xj can handle the JSON body as a stream, the other side-inputs
passed in as XSLT parameters are still supposed to be string. In order to support large
inputs comprehensively, we might need something else than XSLT.
