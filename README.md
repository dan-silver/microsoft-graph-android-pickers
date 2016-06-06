[ ![Download](https://api.bintray.com/packages/dan-silver/maven/microsoft-graph-pickers/images/download.svg) ](https://bintray.com/dan-silver/maven/microsoft-graph-pickers/_latestVersion)

# microsoft-graph-android-pickers
This is an unofficial community project that is not endorsed by Microsoft.

Easy way to get data from the Microsoft Graph into your app using pickers to find users, files and more.

<img src="https://github.com/dan-silver/microsoft-graph-android-pickers/raw/master/resources/file-search.gif" alt="file picker animation" width="300">

## Install

```gradle
// add the following to your build.gradle file to get the package from jCenter
dependencies {
    compile 'msgraph.pickers:graphpickers:1.0.25'
}

// initialize the library with a GraphServiceClient
// follow official Microsoft documentation at https://github.com/microsoftgraph/msgraph-sdk-android

GraphPickerLib.init(mClient);
```



### User search
Requires the ```User.ReadBasic.All``` permission.
```java
// create the search intent
// then launch with startActivityForResult()
GraphUserSearch.IntentBuilder builder = new GraphUserSearch.IntentBuilder();
startActivityForResult(builder.build(context), REQUEST_CODE_FIND_USER);

// Capture the results when a user is selected
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE_FIND_USER) {
        if (resultCode == Activity.RESULT_OK) {
            User user = GraphUserSearch.getUser(data);
            
            // grab user properies
            Log.v("GraphSearch", "Found user " + user.displayName);
        }
    }
}
```

### OneDrive file search
Requires the ```Files.Read``` permission.
```java
// create the search intent
// then launch with startActivityForResult()
GraphFileSearch.IntentBuilder builder = new GraphFileSearch.IntentBuilder();
builder.setShowFileExtensions(true); //optional
startActivityForResult(builder.build(context), REQUEST_CODE_FIND_USER);

// capture results in onActivityResult()
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == Activity.RESULT_OK) {
        if (requestCode == REQUEST_CODE_FIND_FILE) {
            DriveItem file = GraphFileSearch.getFile(data);
            Toast.makeText(context, file.name, Toast.LENGTH_SHORT).show();
        }
    }
}
```

### Mail search
Requires at least the ```Mail.Read``` permission.
```java
// create the search intent
// then launch with startActivityForResult()
    GraphMailSearch.IntentBuilder builder = new GraphMailSearch.IntentBuilder();
    builder.setSearchPlaceholderText("Search email...");
    startActivityForResult(builder.build(context), REQUEST_CODE_FIND_MESSAGE);


// capture results in onActivityResult()
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == Activity.RESULT_OK) {
        if (requestCode == REQUEST_CODE_FIND_MESSAGE) {
            Message msg = GraphMailSearch.getMessage(data);
            Toast.makeText(context, msg.subject, Toast.LENGTH_SHORT).show();
        }
    }
}
```



## Options
Use the builder factory to customize the search page. The following options are available on all builder types.

```java
builder.setSearchPlaceholderText("Search for users in your organization");
builder.setOpenKeyboardByDefault(false);
builder.setDefaultIcon(R.mipmap.custom_image); // shown if user picture not found, unknown file extension, etc.
builder.setDebounceTime(250); // delay in milliseconds between keystrokes before search is processed
```
