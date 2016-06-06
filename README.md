[ ![Download](https://api.bintray.com/packages/dan-silver/maven/microsoft-graph-pickers/images/download.svg) ](https://bintray.com/dan-silver/maven/microsoft-graph-pickers/_latestVersion)

# microsoft-graph-android-pickers
This is an unofficial community project that is not endorsed by Microsoft.

Simple android components to find users, files, and more in the Microsoft Graph.

## Install

```gradle
// add the following to your build.gradle file
dependencies {
    compile 'msgraph.pickers:graphpickers:1.0-beta8'
}

maven {
    url 'https://dl.bintray.com/dan-silver/maven/'
}

```

```java
// initialize the library with a GraphServiceClient
// follow official Microsoft documentation at https://github.com/microsoftgraph/msgraph-sdk-android

GraphPickerLib.init(mClient);
```



### User Search
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
            Toast.makeText(getApplicationContext(), file.name, Toast.LENGTH_SHORT).show();
        }
    }
}
```



## Options
Use the builder factory to customize the search page. All the following options are available on all builder types.

```java
builder.setSearchPlaceholderText("Search for users in your organization");
builder.setOpenKeyboardByDefault(false);
builder.setDefaultIcon(R.mipmap.custom_image); // shown if user picture not found, unknown file extension, etc.
builder.setDebounceTime(250); // delay in milliseconds between keystrokes before search is processed
```