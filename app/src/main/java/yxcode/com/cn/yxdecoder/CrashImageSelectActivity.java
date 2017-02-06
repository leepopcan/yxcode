package yxcode.com.cn.yxdecoder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import yxcode.com.cn.yxdecoder.base.BaseActivity;
import yxcode.com.cn.yxdecoder.utils.FileUtil;

/**
 * Created by lihaifeng on 16/9/14.
 * Desc :
 */
public class CrashImageSelectActivity extends BaseActivity{


    public static final String SELECT_KEY = "SELECT_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("选择crash 图片");

        ListView listView = (ListView) View.inflate(getApplicationContext(),R.layout.activity_crash_image,null);
        List<ItemModel> listData = new ArrayList<ItemModel>();

        try {
            File[] files = FileUtil.listCrashFile();
            for(File f : files){
                if(f.getName().endsWith(".jpg")){
                    listData.add(new ItemModel(f));
                }
            }

            CrashAdapter adapter = new CrashAdapter(getApplicationContext(),listData);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Object itemModel = adapterView.getItemAtPosition(i);
                    if(itemModel instanceof ItemModel){
                        String name = ((ItemModel) itemModel).name;
                        getIntent().putExtra(SELECT_KEY,name.substring(0,name.lastIndexOf(".")));
                        setResult(RESULT_OK,getIntent());
                        finish();
                    }
                }
            });
            setContentView(listView);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public static class ItemModel implements Parcelable{

        Bitmap bitmap;
        String name;

        public ItemModel(){}

        public ItemModel(File file){
            bitmap = BitmapFactory.decodeFile(file.getPath());
            name = file.getName();
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public String getName() {
            return name;
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            if(null != bitmap && !bitmap.isRecycled()){
                bitmap.recycle();
                bitmap = null;
            }
        }

        public int describeContents()
        {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags)
        {
            out.writeString(name);
            bitmap.writeToParcel(out,flags);
        }

        public static final Parcelable.Creator<ItemModel> CREATOR = new Parcelable.Creator<ItemModel>()
        {
            public ItemModel createFromParcel(Parcel in)
            {
                ItemModel pb = new ItemModel();
                pb.name = in.readString();
                pb.bitmap = Bitmap.CREATOR.createFromParcel(in);
                return pb;
            }

            public ItemModel[] newArray(int size)
            {
                return new ItemModel[size];
            }
        };
    }

    private static class CrashAdapter extends BaseAdapter{
        private Context context;
        private List<ItemModel> data;

        public CrashAdapter(Context context,List<ItemModel> list){
            this.context = context.getApplicationContext();
            data = list;
        }

        @Override
        public int getCount() {
            return null == data ? 0 : data.size();
        }

        @Override
        public Object getItem(int i) {
            return null == data ? null : data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Holder holder = null;

            if(null == view){
                view = View.inflate(context,R.layout.crash_list_item,null);
                holder = new Holder();
                holder.imageview = (ImageView) view.findViewById(R.id.crash_bitmap);
                holder.textView = (TextView) view.findViewById(R.id.crash_file_name);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }

            ItemModel model = (ItemModel) getItem(i);

            recycleImageView(holder.imageview);

            holder.imageview.setImageBitmap(model.getBitmap());
            holder.textView.setText(model.getName());

            return view;
        }

        private void recycleImageView(ImageView imageView){
            Drawable drawable = imageView.getDrawable();
            if(null != drawable){
                BitmapDrawable bd = (BitmapDrawable)drawable;
                Bitmap bitmap = bd.getBitmap();
                if(null != bitmap && !bitmap.isRecycled()){
                    bitmap.recycle();
                }
            }
        }
    }

    private static class Holder{
        public ImageView imageview;
        public TextView textView;
    }
}
