GAE Command Line client
=====

The Goal of the project is to create a simple command line client for the Google App Engine datastore, using the [Remote API](https://developers.google.com/appengine/articles/remote_api). The tool provides a shell-like command line interface with the following commands:
 * ```namespace $namespace``` sets the namespace to ```$namespace``` for the following commands.
 * ```print $key``` print a datastore entity. ```$key``` is the datastore key, in the form KEY($kind, $name) where ```$kind``` is the  entity's kind and ```$name``` the entity's name. This command loads an entity from the datastore, and print it in JSON format (print all the properties in JSON, including null properties)
 * ```put $key $json``` store a datastore entity. ```$key``` is the datastore key (same format as above) and ```$json``` is the entity content in JSON.
 * ```list $kind [$json] [$properties]``` query the datastore and print the list of matching entities.
   - ```$kind``` is the entity kind
   - ```$json``` (if present) specifies a list of filters to apply to the search query
   - ```$properties``` is the list of properties to print in the output list. If not present, print only the decoded entity keys (in the form ```KEY($kind, $name)``` )

The initial purpose of this tool was to do technical support for customers. [Altirnao](http://www.altirnao.com/) has a [SaaS application](http://www.altirnao.com/products/documents-management/) running on App Engine that does Document Management on top of Google Apps. This application's data is on GAE and from time to time support engineers need to make a modification directly in the datastore to fix a problem or to help a customer.

Examples of usage
======
Set namespace:
```
> namespace "-GLOBAL-"
namespace is now "-GLOBAL-"
```

Load entity:
```
> print KEY("Domain", "mycustomer.com")
{
  "domainAdmin" : "admin@mycustomer.com",
  "maxNbUsers" : 12,
  "trialExpires" : "2013-05-31 12:43:02",
  "status" : "ACTIVE"
}
```

Save entity:
```
> put KEY("Domain", "mycustomer.com") {
  "domainAdmin" : "admin@mycustomer.com",
  "maxNbUsers" : 12,
  "trialExpires" : "2013-06-30 00:00:00",
  "status" : "ACTIVE"
}
entity saved successfully at KEY("Domain", "mycustomer.com")
```

Using lists:
```
> print KEY("Role", "00123412ABCD")
{
  "name" : "Administrators",
  "members" : [ "user1@domain.com", "user2@domain.com" ]
}
```

```
> put KEY("Role", "00123412ABCD") {
  "name" : "Administrators",
  "members" : [ "user1@domain.com", "user2@domain.com", "user3@domain.com" ]
}
entity saved successfully at KEY("Role", "00123412ABCD")
```

List all the keys of type "Domain":
```
> list Domain
{ key : KEY("Domain", "abcd.com") }
{ key : KEY("Domain", "boo.com") }
...
{ key : KEY("Domain", "xyz.com") }
```

List only active "Domain"s:
```
> list Domain { "status" : "ACTIVE" }
{ key : KEY("Domain", "abcd.com") }
...
{ key : KEY("Domain", "xyz.com") }
```

Print the ACTIVE domains with the maxNbUsers and the trialExpires property:
```
> list Domain { "status" : "ACTIVE" } [ "maxNbUsers", "trialExpires" ]
{ key : KEY("Domain", "abcd.com"), "maxNbUsers" : 34, "trialExpires" : "2013-03-21 10:28:34" }
...
{ key : KEY("Domain", "xyz.com"), "maxNbUsers", 89, "trialExpires" : NULL }
```

Complete list of supported ​data types:
===
* Int
* Float
* Boolean
* String
* Text
* Date ([format](http://www.w3schools.com/jsref/jsref_tojson.asp))
* Time
* DateTime
* User
* List
* StringList

Build dependencies
===
- commons-cli-1.2
- commons-io-2.4
- jflex-1.4.3