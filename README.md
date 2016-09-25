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

Note: in order to use this tool you need to enable remote API for a particular project as described in this document https://developers.google.com/appengine/docs/java/tools/remoteapi

Known issues
======
[Application-specific password](https://support.google.com/accounts/answer/185833) should be used in case of 2-step verification on google account. Otherwise LoginException with reason "BadAuthentication InvalidSecondFactor" will be thrown.

How to install
======
```
# build
gradle installDist

# run
./build/install/gaecl/bin/gaeshell --help
```

Examples of usage
======
Set namespace:
```
> namespace -GLOBAL-;
namespace is now "-GLOBAL-"
```

Load entity:
```
> print key("Domain", "mycustomer.com");
{
  "domainAdmin" : "admin@mycustomer.com",
  "maxNbUsers" : 12,
  "trialExpires" : "2013-05-31 12:43:02",
  "status" : "ACTIVE"
}
```

Save entity:
```
> put key("Domain", "mycustomer.com") {
  "domainAdmin" : "admin@mycustomer.com",
  "maxNbUsers" : 12,
  "trialExpires" : "2013-06-30 00:00:00",
  "status" : "ACTIVE"
};
entity saved successfully at key("Domain", "mycustomer.com")
```

Using lists:
```
> print key("Role", "00123412ABCD");
{
  "name" : "Administrators",
  "members" : [ "user1@domain.com", "user2@domain.com" ]
}
```

```
> put key("Role", "00123412ABCD") {
  "name" : "Administrators",
  "members" : [ "user1@domain.com", "user2@domain.com", "user3@domain.com" ]
};
entity saved successfully at key("Role", "00123412ABCD")
```

List all the keys of type "Domain":
```
> list Domain;
{ key : key("Domain", "abcd.com") }
{ key : key("Domain", "boo.com") }
...
{ key : key("Domain", "xyz.com") }
```

List only active "Domain"s:
```
> list Domain { "status" : "ACTIVE" };
{ key : key("Domain", "abcd.com") }
...
{ key : key("Domain", "xyz.com") }
```

Print the ACTIVE domains with the maxNbUsers and the trialExpires property:
```
> list Domain { "status" : "ACTIVE" } [ "maxNbUsers", "trialExpires" ];
{ key : key("Domain", "abcd.com"), "maxNbUsers" : 34, "trialExpires" : "2013-03-21 10:28:34" }
...
{ key : key("Domain", "xyz.com"), "maxNbUsers", 89, "trialExpires" : NULL }
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
