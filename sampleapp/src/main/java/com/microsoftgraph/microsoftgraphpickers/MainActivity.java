package com.microsoftgraph.microsoftgraphpickers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.microsoft.graph.authentication.MSAAuthAndroidAdapter;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.core.DefaultClientConfig;
import com.microsoft.graph.core.IClientConfig;
import com.microsoft.graph.extensions.DriveItem;
import com.microsoft.graph.extensions.GraphServiceClient;
import com.microsoft.graph.extensions.IGraphServiceClient;
import com.microsoft.graph.extensions.Message;
import com.microsoft.graph.extensions.User;
import com.microsoftgraph.graphpickers.GraphFileSearch;
import com.microsoftgraph.graphpickers.GraphMailSearch;
import com.microsoftgraph.graphpickers.GraphPickerLib;
import com.microsoftgraph.graphpickers.GraphUserSearch;

public class MainActivity extends AppCompatActivity {
    public static int REQUEST_CODE_FIND_USER = 1;
    public static int REQUEST_CODE_FIND_FILE = 2;
    public static int REQUEST_CODE_FIND_MESSAGE = 3;

    public static IGraphServiceClient mClient;
    public static MSAAuthAndroidAdapter authenticationAdapter;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_FIND_USER) {
                User user = GraphUserSearch.getUser(data);
                Toast.makeText(getApplicationContext(), getString(R.string.found_user, user.displayName), Toast.LENGTH_SHORT).show();
            } else if (requestCode == REQUEST_CODE_FIND_FILE) {
                DriveItem file = GraphFileSearch.getFile(data);
                Toast.makeText(getApplicationContext(), getString(R.string.found_file, file.name), Toast.LENGTH_SHORT).show();
            } else if (requestCode == REQUEST_CODE_FIND_MESSAGE) {
                Message msg = GraphMailSearch.getMessage(data);
                Toast.makeText(getApplicationContext(), getString(R.string.found_item, msg.subject), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_app);

        final Button btn = (Button) findViewById(R.id.search_users_btn);
        if (btn != null) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GraphUserSearch.IntentBuilder builder = new GraphUserSearch.IntentBuilder();
                    builder.setDebounceTime(250);
                    builder.setSearchPlaceholderText("Find users...");
                    startActivityForResult(builder.build(getApplicationContext()), REQUEST_CODE_FIND_USER);
                }
            });
        }

        Button btnDriveSearch = (Button) findViewById(R.id.search_drive_btn);
        if (btnDriveSearch != null) {
            btnDriveSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GraphFileSearch.IntentBuilder builder = new GraphFileSearch.IntentBuilder();
                    builder.setDebounceTime(250);
                    builder.setSearchPlaceholderText("Find files...");
                    builder.setShowFileExtensions(true);
                    startActivityForResult(builder.build(getApplicationContext()), REQUEST_CODE_FIND_FILE);
                }
            });
        }


        Button btnMailSearch = (Button) findViewById(R.id.search_mail_btn);
        if (btnMailSearch != null) {
            btnMailSearch.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    GraphMailSearch.IntentBuilder builder = new GraphMailSearch.IntentBuilder();
                    builder.setSearchPlaceholderText("Search email...");
                    builder.setOpenKeyboardByDefault(false);
                    startActivityForResult(builder.build(getApplicationContext()), REQUEST_CODE_FIND_MESSAGE);
                }
            });
        }

        authenticationAdapter = new MSAAuthAndroidAdapter(getApplication()) {
            @Override
            public String getClientId() {
                return getString(R.string.appId);
            }

            @Override
            public String[] getScopes() {
                return new String[] {
                        "https://graph.microsoft.com/User.ReadBasic.All",
                        "https://graph.microsoft.com/Files.Read",
                        "https://graph.microsoft.com/Mail.Read",
                };
            }
        };

        authenticationAdapter.login(this, new ICallback<Void>() {
            @Override
            public void success(final Void aVoid) {
                GraphPickerLib.init(mClient);

            }

            @Override
            public void failure(final ClientException ex) {
                //Handle failed login
            }
        });

        IClientConfig mClientConfig = DefaultClientConfig.createWithAuthenticationProvider(authenticationAdapter);
        mClient = new GraphServiceClient
                .Builder()
                .fromConfig(mClientConfig)
                .buildClient();



    }
}
