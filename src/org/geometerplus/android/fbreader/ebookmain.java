package org.geometerplus.android.fbreader;

import java.io.FileInputStream;



import android.app.Activity;
import android.os.Bundle;


import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
//import android.preference.PreferenceActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;


import com.sean.android.ebookmain.R;

import org.geometerplus.android.fbreader.FBReader;
import org.geometerplus.android.fbreader.library.LibraryRecentActivity;
import org.geometerplus.android.fbreader.library.LibraryTopLevelActivity;
import org.geometerplus.android.fbreader.network.NetworkLibraryActivity;
import org.geometerplus.android.fbreader.preferences.PreferenceActivity;

import android.util.Log;
import android.os.Handler;
import android.os.HandlerThread;
import java.util.ArrayList;
import android.view.Window;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * This demo shows how various action bar display option flags can be combined and their effects.
 */
public class ebookmain extends Activity
        implements View.OnClickListener {
    private View mCustomView;
    private static final String TAG = "ebookmain";

    private Button mlocal;
    private Button mnetwork;
    private Button mbookmark;
    private Button msetting;  

    private FBReader mFBReader;

    private Context mContext;
     ImageView imageView;
     TextView  textview;
/*    private Integer[] mImageIds = {
             R.drawable.wallpaper_jellyfish,
             R.drawable.wallpaper_lake,
             R.drawable.wallpaper_mountain,
             R.drawable.wallpaper_path,
             R.drawable.wallpaper_road,
             R.drawable.wallpaper_snow_leopard,
             R.drawable.wallpaper_sunrise,
             R.drawable.wallpaper_sunset            
       };*/

    private Integer[] mImageIds = {
            R.drawable.favorites,
            R.drawable.author,
            R.drawable.title,
            R.drawable.readermain,
            R.drawable.recent,
            R.drawable.booktag,
            R.drawable.filetree,
            R.drawable.network
    };
    private Integer[] mImageIds_main = {
            R.drawable.favorites_m,
            R.drawable.author_m,
            R.drawable.title_m,
            R.drawable.readermain_m,
            R.drawable.recent_m,
            R.drawable.booktag_m,
            R.drawable.filetree_m,
            R.drawable.network_m
    };
    
    
    
    private static final HandlerThread sWorkerThread = new HandlerThread("ebook-main");
    static {
        sWorkerThread.start();
    }
    private static final Handler sWorker = new Handler(sWorkerThread.getLooper());

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//          requestWindowFeature(Window.FEATURE_ACTION_BAR); 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
 //       setContentView(R.layout.action_bar_display_options);
        setContentView(R.layout.ebookmain);
 
        //Use this if you want to use XML layout file
        //setContentView(R.layout.main);
        //coverFlow =  (CoverFlow) findViewById(R.id.coverflow);
        CoverFlow coverFlow;
        coverFlow =  (CoverFlow) findViewById(R.id.coverflow);
        ImageAdapter coverImageAdapter =  new ImageAdapter(this);
        
        coverImageAdapter.createReflectedImages();
        
        coverFlow.setAdapter(coverImageAdapter);

        textview = (TextView)findViewById(R.id.TextView01);
        imageView = (ImageView)findViewById(R.id.ImageView01);
        textview.setText("Sea Reader Main");  
        imageView.setImageResource(mImageIds_main[3]);
        
        coverFlow.setOnItemClickListener(new CoverAdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(CoverAdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
/*                Toast.makeText(getBaseContext(), 
                        "You have selected picture " + (arg2+1) + " of Antartica", 
                        Toast.LENGTH_SHORT).show();*/
                switch (arg2+1) {
                    case 1:
                        textview.setText("Favorites List");
                        break;
                    case 2:
                        textview.setText("By Author List");
                        break;
                    case 3:
                        textview.setText("By Title List");  
                        break;
                    case 4:
                        textview.setText("Sea Reader Main");  
                        break; 
                    case 5:
                        textview.setText("Recent List");  
                        break; 
                    case 6:
                        textview.setText("By Tag List");  
                        break;   
                    case 7:
                        textview.setText("File Tree");  
                        break;  
                    case 8:
                        textview.setText("Network Library");  
                        break;                          
                    default:                
                        break;
                }               
                
                imageView.setImageResource(mImageIds_main[arg2]);
                
            }
            
        });
        
        
        coverFlow.setSpacing(-15);
        coverFlow.setSelection(3, true);
        
        mContext=this;






      
        
        
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.v(TAG, "SEAN_LOG  onPrepareOptionsMenu " );  
        // TODO Auto-generated method stub
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(TAG, "SEAN_LOG  onCreateOptionsMenu " );  
        getMenuInflater().inflate(R.menu.display_options_actions, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        Log.v(TAG, "SEAN_LOG  onOptionsItemSelected " +item.getItemId());       
        switch (item.getItemId()) {
            case R.id.show_library:
                onClickLocalLibraryButton();
                break;
            case R.id.network_library:
                onClickNetworkLibraryButton(); 
                break;
/*            case R.id.show_preference:
                onClickSettingButton();
                break;*/
 
            default:                
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    public void onClick(View v) {
/*        final ActionBar bar = getActionBar();
        int flags = 0;
        switch (v.getId()) {
            case R.id.toggle_home_as_up:
                flags = ActionBar.DISPLAY_HOME_AS_UP;
                break;
            case R.id.toggle_show_home:
                flags = ActionBar.DISPLAY_SHOW_HOME;
                break;
            case R.id.toggle_use_logo:
                flags = ActionBar.DISPLAY_USE_LOGO;
                break;
            case R.id.toggle_show_title:
                flags = ActionBar.DISPLAY_SHOW_TITLE;
                break;
            case R.id.toggle_show_custom:
                flags = ActionBar.DISPLAY_SHOW_CUSTOM;
                break;

            case R.id.toggle_navigation:
                bar.setNavigationMode(
                        bar.getNavigationMode() == ActionBar.NAVIGATION_MODE_STANDARD
                                ? ActionBar.NAVIGATION_MODE_TABS
                                : ActionBar.NAVIGATION_MODE_STANDARD);
                return;
            case R.id.cycle_custom_gravity:
                ActionBar.LayoutParams lp = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
                int newGravity = 0;
                switch (lp.gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.LEFT:
                        newGravity = Gravity.CENTER_HORIZONTAL;
                        break;
                    case Gravity.CENTER_HORIZONTAL:
                        newGravity = Gravity.RIGHT;
                        break;
                    case Gravity.RIGHT:
                        newGravity = Gravity.LEFT;
                        break;
                }
                lp.gravity = lp.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | newGravity;
                bar.setCustomView(mCustomView, lp);
                return;
        }

        int change = bar.getDisplayOptions() ^ flags;
        bar.setDisplayOptions(change, flags);*/
    }


    private void initializeButton() {
 /*       
        mlocal = (Button)findViewById(R.id.local_library);
        mlocal.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
              onClickLocalLibraryButton();
            }
        });
        
        mnetwork = (Button)findViewById(R.id.network_library);
        mnetwork.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               onClickNetworkLibraryButton();
            }
        });

        mbookmark = (Button)findViewById(R.id.bookmark);
        mbookmark.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
             onClickBookmarkButton();
            }
        });

        msetting = (Button)findViewById(R.id.settings);
        msetting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            onClickSettingButton();
            }
        });
 */       
    }
    private void onClickLocalLibraryButton() {
        Intent intent = new Intent(this, LibraryTopLevelActivity.class);
        this.startActivity(intent);
        
    }     
    private void onClickNetworkLibraryButton() {
        Intent intent = new Intent(this, NetworkLibraryActivity.class);
        this.startActivity(intent);
        
    }           
/*     private void onClickSettingButton() {
//         Intent intent = new Intent(this, PreferenceActivity.class);
        Intent intent = new Intent(this, LibraryRecentActivity.class);
         this.startActivity(intent);
        
    }  */


     
    private void setAllButtonInVisible() {
      
/*        Button Button_A=(Button)findViewById(R.id.toggle_home_as_up);
        Button Button_B=(Button)findViewById(R.id.toggle_show_home);
        Button Button_C=(Button)findViewById(R.id.toggle_use_logo);
        Button Button_D=(Button)findViewById(R.id.toggle_show_title);
        Button Button_E=(Button)findViewById(R.id.toggle_show_custom);
        Button Button_F=(Button)findViewById(R.id.toggle_navigation);
        Button Button_G=(Button)findViewById(R.id.cycle_custom_gravity);
        
               
        ((Button) Button_A).setVisibility(View.INVISIBLE);
        ((Button) Button_B).setVisibility(View.INVISIBLE);  
        ((Button) Button_C).setVisibility(View.INVISIBLE);
        ((Button) Button_D).setVisibility(View.INVISIBLE);
        ((Button) Button_E).setVisibility(View.INVISIBLE);        
        ((Button) Button_F).setVisibility(View.INVISIBLE);    
        ((Button) Button_G).setVisibility(View.INVISIBLE); */     

       
/*        mlocal=(Button)findViewById(R.id.local_library);
        mnetwork=(Button)findViewById(R.id.network_library);
        mbookmark=(Button)findViewById(R.id.bookmark);
        msetting=(Button)findViewById(R.id.settings);
        
        ((Button) mlocal).setVisibility(View.INVISIBLE);
        ((Button) mnetwork).setVisibility(View.INVISIBLE);  
        ((Button) mbookmark).setVisibility(View.INVISIBLE);
        ((Button) msetting).setVisibility(View.INVISIBLE);*/

    }    
    private void setAllButtonVisible() {
       
 /*       Button Button_A=(Button)findViewById(R.id.toggle_home_as_up);
        Button Button_B=(Button)findViewById(R.id.toggle_show_home);
        Button Button_C=(Button)findViewById(R.id.toggle_use_logo);
        Button Button_D=(Button)findViewById(R.id.toggle_show_title);
        Button Button_E=(Button)findViewById(R.id.toggle_show_custom);
        Button Button_F=(Button)findViewById(R.id.toggle_navigation);
        Button Button_G=(Button)findViewById(R.id.cycle_custom_gravity);
        
               
        ((Button) Button_A).setVisibility(View.VISIBLE);
        ((Button) Button_B).setVisibility(View.VISIBLE);  
        ((Button) Button_C).setVisibility(View.VISIBLE);
        ((Button) Button_D).setVisibility(View.VISIBLE);
        ((Button) Button_E).setVisibility(View.VISIBLE);        
        ((Button) Button_F).setVisibility(View.VISIBLE);    
        ((Button) Button_G).setVisibility(View.VISIBLE);    */     
   
 
/*        mlocal=(Button)findViewById(R.id.local_library);
        mnetwork=(Button)findViewById(R.id.network_library);
        mbookmark=(Button)findViewById(R.id.bookmark);
        msetting=(Button)findViewById(R.id.settings);
        ((Button) mlocal).setVisibility(View.VISIBLE);
        ((Button) mnetwork).setVisibility(View.VISIBLE);  
        ((Button) mbookmark).setVisibility(View.VISIBLE);
        ((Button) msetting).setVisibility(View.VISIBLE);*/
      
    }        

    private void setHomeVisible() {
//        final ActionBar barHome = getActionBar();
//        int flags = 0;
       
 /*       
        switch (v.getId()) {
            case R.id.toggle_home_as_up:
                flags = ActionBar.DISPLAY_HOME_AS_UP;
                break;
            case R.id.toggle_show_home:
                flags = ActionBar.DISPLAY_SHOW_HOME;
                break;
            case R.id.toggle_use_logo:
                flags = ActionBar.DISPLAY_USE_LOGO;
                break;
            case R.id.toggle_show_title:
                flags = ActionBar.DISPLAY_SHOW_TITLE;
                break;
            case R.id.toggle_show_custom:
                flags = ActionBar.DISPLAY_SHOW_CUSTOM;
                break;

            case R.id.toggle_navigation:
                bar.setNavigationMode(
                        bar.getNavigationMode() == ActionBar.NAVIGATION_MODE_STANDARD
                                ? ActionBar.NAVIGATION_MODE_TABS
                                : ActionBar.NAVIGATION_MODE_STANDARD);
                return;
            case R.id.cycle_custom_gravity:
                ActionBar.LayoutParams lp = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
                int newGravity = 0;
                switch (lp.gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.LEFT:
                        newGravity = Gravity.CENTER_HORIZONTAL;
                        break;
                    case Gravity.CENTER_HORIZONTAL:
                        newGravity = Gravity.RIGHT;
                        break;
                    case Gravity.RIGHT:
                        newGravity = Gravity.LEFT;
                        break;
                }
                lp.gravity = lp.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | newGravity;
                barHome.setCustomView(mCustomView, lp);
                return;
        }
*/
//        flags = ActionBar.DISPLAY_SHOW_TITLE;  
//        int change = barHome.getDisplayOptions() ^ flags;
//        barHome.setDisplayOptions(change, flags);
        
    }   
    
    public class ImageAdapter extends BaseAdapter {
        int mGalleryItemBackground;
        private Context mContext;

/*     private Integer[] mImageIds = {
      R.drawable.wallpaper_jellyfish,
      R.drawable.wallpaper_lake,
      R.drawable.wallpaper_mountain,
      R.drawable.wallpaper_path,
      R.drawable.wallpaper_road,
      R.drawable.wallpaper_snow_leopard,
      R.drawable.wallpaper_sunrise
};*/

//        private Integer[] mImageIds = {
//                R.drawable.kasabian_kasabian,
//                R.drawable.starssailor_silence_is_easy,
//                R.drawable.killers_day_and_age,
//                R.drawable.garbage_bleed_like_me,
//                R.drawable.death_cub_for_cutie_the_photo_album,
//                R.drawable.kasabian_kasabian,
//                R.drawable.massive_attack_collected,
//                R.drawable.muse_the_resistance,
//                R.drawable.starssailor_silence_is_easy
//        };

        private ImageView[] mImages;
        
        public ImageAdapter(Context c) {
            mContext = c;
            mImages = new ImageView[mImageIds.length];
        }
        public boolean createReflectedImages() {
                //The gap we want between the reflection and the original image
                final int reflectionGap = 4;
                
                
                int index = 0;
                for (int imageId : mImageIds) {
                    Bitmap originalImage = BitmapFactory.decodeResource(getResources(), 
                            imageId);
                    int width = originalImage.getWidth();
                    int height = originalImage.getHeight();
                    
           
                    //This will not scale but will flip on the Y axis
                    Matrix matrix = new Matrix();
                    matrix.preScale(1, -1);
                    
                    //Create a Bitmap with the flip matrix applied to it.
                    //We only want the bottom half of the image
                    Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height/2, width, height/2, matrix, false);
                    
                        
                    //Create a new bitmap with same width but taller to fit reflection
                    Bitmap bitmapWithReflection = Bitmap.createBitmap(width 
                      , (height + height/2), Config.ARGB_8888);
                  
                   //Create a new Canvas with the bitmap that's big enough for
                   //the image plus gap plus reflection
                   Canvas canvas = new Canvas(bitmapWithReflection);
                   //Draw in the original image
                   canvas.drawBitmap(originalImage, 0, 0, null);
                   //Draw in the gap
                   Paint deafaultPaint = new Paint();
                   canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);
                   //Draw in the reflection
                   canvas.drawBitmap(reflectionImage,0, height + reflectionGap, null);
                   
                   //Create a shader that is a linear gradient that covers the reflection
                   Paint paint = new Paint(); 
                   LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0, 
                     bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, 
                     TileMode.CLAMP); 
                   //Set the paint to use this shader (linear gradient)
                   paint.setShader(shader); 
                   //Set the Transfer mode to be porter duff and destination in
                   paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN)); 
                   //Draw a rectangle using the paint with our linear gradient
                   canvas.drawRect(0, height, width, 
                     bitmapWithReflection.getHeight() + reflectionGap, paint); 
                   
                   ImageView imageView = new ImageView(mContext);
                   imageView.setImageBitmap(bitmapWithReflection);
                   imageView.setLayoutParams(new CoverFlow.LayoutParams(120, 180));
                   imageView.setScaleType(ScaleType.MATRIX);
                   mImages[index++] = imageView;
                   
                }
                return true;
        }

        public int getCount() {
            return mImageIds.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            //Use this code if you want to load from resources
//            ImageView i = new ImageView(mContext);
//            i.setImageResource(mImageIds[position]);
//            i.setLayoutParams(new CoverFlow.LayoutParams(130, 130));
//            i.setScaleType(ImageView.ScaleType.MATRIX);           
//            return i;
            
            return mImages[position];
        }
         /** Returns the size (0.0f to 1.0f) of the views 
         * depending on the 'offset' to the center. */ 
         public float getScale(boolean focused, int offset) { 
           /* Formula: 1 / (2 ^ offset) */ 
             return Math.max(0, 1.0f / (float)Math.pow(2, Math.abs(offset))); 
         } 

    }
    public void RoadRecentBook() {
        enqueuePackageUpdated(new PackageUpdatedTask(PackageUpdatedTask.OP_LOAD_RECENT_BOOK));

    }
    
    void enqueuePackageUpdated(PackageUpdatedTask task) {
        sWorker.post(task);
    }

    
    private class PackageUpdatedTask implements Runnable {
        int mOp;

        public static final int OP_NONE = 0;
        public static final int OP_LOAD_RECENT_BOOK = 1;

        public static final int OP_UNAVAILABLE = 2; 

        public PackageUpdatedTask(int op) {
            mOp = op;
   
        }

        public void run() {
            final Context context = mContext;

   
            switch (mOp) {
                case OP_LOAD_RECENT_BOOK:

                    break;
 

                case OP_UNAVAILABLE:

                    break;
            }

        }
    }    
    
}
