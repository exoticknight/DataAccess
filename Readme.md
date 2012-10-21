#DataAccess
##A Java class that helps you easily access to Access Database.
###In fact, you can use it to access to any database that Java supports by making a few changes.

usage:

```
DataAccess myData = new DataAccess("database.mdb");
Vector<String[]> result;
// query, returns a Vector<String[]>
result = myData.query("SELECT * FROM youTable");
// insert/delete/update, returns the ID of the last modified tuple
myData.exec("INSERT INTO youTable VALUES('youdata1','youdata2')");
```

