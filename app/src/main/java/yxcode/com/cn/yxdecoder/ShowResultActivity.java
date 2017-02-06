package yxcode.com.cn.yxdecoder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import yxcode.com.cn.yxdecoder.account.AccountManager;
import yxcode.com.cn.yxdecoder.base.APP;
import yxcode.com.cn.yxdecoder.base.BaseActivity;
import yxcode.com.cn.yxdecoder.base.LoginActivity;
import yxcode.com.cn.yxdecoder.network.IService;
import yxcode.com.cn.yxdecoder.network.model.ApplyModel;
import yxcode.com.cn.yxdecoder.network.model.ExtraDetailDto;
import yxcode.com.cn.yxdecoder.network.model.ResponseModel;
import yxcode.com.cn.yxdecoder.utils.BaseSubscriber;
import yxcode.com.cn.yxdecoder.utils.DeviceUtils;
import yxcode.com.cn.yxdecoder.utils.RetrofitClient;
import yxcode.com.cn.yxdecoder.utils.ToastUtils;
import yxcode.com.cn.yxdecoder.widget.ColorLabel;
import yxcode.com.cn.yxdecoder.widget.LabelValue;


public class ShowResultActivity extends BaseActivity {


	String keyInfo;
	String content;
	APP.DECODE_MODE decodeMode;
	List<ExtraDetailDto.ExtraDetail> extraDetailList = new ArrayList<>();
	List<ExtraDetailDto.Pair> scanResult = new ArrayList<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

		if(null == loginReceiver){
			loginReceiver = new LoginReceiver();
		}
		localBroadcastManager.registerReceiver(loginReceiver,new IntentFilter(AccountManager.LOGIN_ACTION));

		checkDeviceRight();

		setContentView(R.layout.activity_show_result);

		container = (LinearLayout) findViewById(R.id.container);


		Bundle extras = getIntent().getExtras();
		String type = extras.getString(APP.TYPE);
		content = extras.getString(APP.CONTENT);
		keyInfo = extras.getString(APP.KEYINFO);
		String cost = extras.getString(APP.COST);
		fetchCodeInfo();



		ExtraDetailDto.ExtraDetail resultDetail = new ExtraDetailDto.ExtraDetail();
		extraDetailList.add(resultDetail);
		ExtraDetailDto.Pair contentPair = new ExtraDetailDto.Pair();
		ExtraDetailDto.Pair costPair = new ExtraDetailDto.Pair();
		ExtraDetailDto.Pair keyInfoPair = new ExtraDetailDto.Pair();

		resultDetail.setTitle("扫描结果");
		resultDetail.setExtraDetailList(scanResult);


		costPair.setKey("花费时间");
		costPair.setValue(cost + "ms");
		if(!TextUtils.isEmpty(type)){
			decodeMode = APP.DECODE_MODE.valueOf(type);
			if(null != decodeMode){
				switch (decodeMode){
					case QRCODE:
						contentPair.setKey("内码扫描结果");
						contentPair.setValue(content);
						break;
					case YXCODE:
						contentPair.setKey("央信码扫描结果");
						contentPair.setValue(content);
						break;
					case FIRST:
						if(!TextUtils.isEmpty(content)){//内码
							contentPair.setKey("内码扫描结果");
							contentPair.setValue(content);
						} else if(!TextUtils.isEmpty(keyInfo)){//央信码
							keyInfoPair.setKey("央信码扫描结果");
							keyInfoPair.setValue(keyInfo);
						}
						break;
					case BOTH:
						if(!TextUtils.isEmpty(content)){//内码
							contentPair.setKey("内码扫描结果");
							contentPair.setValue(content);
						}
						if(!TextUtils.isEmpty(keyInfo)){//央信码
							keyInfoPair.setKey("央信码扫描结果");
							keyInfoPair.setValue(keyInfo);
						}
						break;
				}
			}
		} else {
			// failed
			contentPair.setKey("扫描失败");
			contentPair.setValue(content);
		}

		if(!contentPair.isEmpty()){
			scanResult.add(contentPair);
		}
		if(!keyInfoPair.isEmpty()){
			scanResult.add(keyInfoPair);
		}
		scanResult.add(costPair);

		showResult(extraDetailList);
	}

	private void fetchCodeInfo(){
		if(!AccountManager.isLogin(getApplicationContext()) || !hasPermission){
			return;
		}
		if(decodeMode == APP.DECODE_MODE.QRCODE || decodeMode == APP.DECODE_MODE.YXCODE){
			return;
		}
		doFetch();
	}

	private void doFetch(){
		IService iService = RetrofitClient.getInstance(getApplicationContext()).create(IService.class);
		RetrofitClient.execute(
				iService.fetchCodeInfo(keyInfo,content),
				new BaseSubscriber<ExtraDetailDto>(getApplicationContext()) {
					@Override
					public void onNext(ExtraDetailDto extraDetailDto) {
						super.onNext(extraDetailDto);
						if (extraDetailDto.isSuccess()) {
							showReportResult(extraDetailDto);
						} else {
							ExtraDetailDto.Pair costPair = new ExtraDetailDto.Pair();
							costPair.setKey("错误信息");
							costPair.setValue(extraDetailDto.getErrorMsg());
							scanResult.add(costPair);
							showResult(extraDetailList);
//							showError(extraDetailDto.getErrorMsg());
//							Toast.makeText(getApplicationContext(), extraDetailDto.getErrorMsg(), Toast.LENGTH_LONG).show();
						}
					}
					@Override
					public void onError(Throwable e) {
						super.onError(e);
						Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
					}
				});
	}

	LinearLayout container;

//	private void showError(String errorMsg){
//		container.removeAllViews();
//		TextView textView = new TextView(getApplicationContext());
//		textView.setText(errorMsg);
//		textView.setTextColor(Color.BLACK);
//		container.addView(textView);
//	}

	private void showReportResult(ExtraDetailDto extraDetailDto){
		if(null == extraDetailDto){
			return;
		}
		List<ExtraDetailDto.ExtraDetail>  list = extraDetailDto.getData().getCodeDetailList();
		list.addAll(0,extraDetailList);
		showResult(list);
	}

	private void showResult(List<ExtraDetailDto.ExtraDetail> extraDetailList){
		container.removeAllViews();
		ListIterator<ExtraDetailDto.ExtraDetail> listIterator = extraDetailList.listIterator();
		while(listIterator.hasNext()){
			createItem(listIterator.next());
		}
	}

	private void createItem(ExtraDetailDto.ExtraDetail extraDetail){
		ColorLabel colorLabel = new ColorLabel(getApplicationContext());
		colorLabel.getTextView().setText(extraDetail.getTitle());

		container.addView(colorLabel);
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) colorLabel.getLayoutParams();
		lp.bottomMargin = 10;
		lp.topMargin = 10;
		lp.leftMargin=20;


		List<ExtraDetailDto.Pair> pairList = extraDetail.getExtraDetailList();
		ListIterator<ExtraDetailDto.Pair> listIterator = pairList.listIterator();
		while(listIterator.hasNext()){
			createPair(listIterator.next());
		}
	}

	private void createPair(ExtraDetailDto.Pair pair){
		LabelValue labelValue = new LabelValue(getApplicationContext());
		labelValue.getTitleView().setText(pair.getKey());
		labelValue.getValueView().setText(pair.getValue());
		container.addView(labelValue);

		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) labelValue.getLayoutParams();
		lp.leftMargin = 40;
	}

	/************************************/
	private void apply(){
		String token = AccountManager.getToken(getApplicationContext());
		if(TextUtils.isEmpty(token)){
			return;
		}

		IService iService = RetrofitClient.getInstance(getApplicationContext()).create(IService.class);
		RetrofitClient.execute(
				iService.apply(token, DeviceUtils.getDeviceId(getApplicationContext()),"android"),
				new BaseSubscriber<ApplyModel>(getApplicationContext()) {
					@Override
					public void onNext(ApplyModel applyModel) {
						super.onNext(applyModel);
						if (applyModel.isSuccess()) {
							hasPermission = applyModel.isData();
							invalidateOptionsMenu();
						} else {
							Toast.makeText(getApplicationContext(), applyModel.getErrorMsg(), Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void onError(Throwable e) {
						super.onError(e);
						Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
					}
				});
	}

	boolean hasPermission = false;

	private void checkDeviceRight(){
		String token = AccountManager.getToken(getApplicationContext());
		if(TextUtils.isEmpty(token)){
			return;
		}

		IService iService = RetrofitClient.getInstance(getApplicationContext()).create(IService.class);
		RetrofitClient.execute(
				iService.checkDeviceRight(token, DeviceUtils.getDeviceId(getApplicationContext()),"android"),
				new BaseSubscriber<ApplyModel>(getApplicationContext()) {
					@Override
					public void onNext(ApplyModel applyModel) {
						super.onNext(applyModel);
						if (applyModel.isSuccess()) {
							hasPermission = applyModel.isData();
							invalidateOptionsMenu();
						} else {
							Toast.makeText(getApplicationContext(), applyModel.getErrorMsg(), Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void onError(Throwable e) {
						super.onError(e);
						Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
					}
				});
	}

	private void reportCode(){

		if(!getIntent().hasExtra(IService.CONTENT) ||
				!getIntent().hasExtra(IService.INNERCODETYPE) ||
				!getIntent().hasExtra(IService.KEYINFO)){
			return;
		}

		Context context = getApplicationContext();
		String content = getIntent().getStringExtra(IService.CONTENT);
		String innerCodeType = getIntent().getStringExtra(IService.INNERCODETYPE);
		String keyInfo = getIntent().getStringExtra(IService.KEYINFO);
		String token = AccountManager.getToken(context);

		IService iService = RetrofitClient.getInstance(getApplicationContext()).create(IService.class);
		RetrofitClient.execute(iService.reportCode(token, content, innerCodeType, keyInfo), new BaseSubscriber<ResponseModel>(getApplicationContext()) {
			@Override
			public void onNext(ResponseModel responseModel) {
				super.onNext(responseModel);
				ToastUtils.mkShortTimeToast(getApplicationContext(),responseModel.getErrorMsg());
			}

			@Override
			public void onError(Throwable e) {
				super.onError(e);
				ToastUtils.mkShortTimeToast(getApplicationContext(),e.getMessage());
			}

			@Override
			public void onStart() {
				super.onStart();
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_login){
			LoginActivity.launch(this);
		} else if(item.getItemId() == R.id.action_permission){
			apply();
		}
		return super.onOptionsItemSelected(item);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.scan_result_menu, menu);

		if(AccountManager.isLogin(getBaseContext())){
			menu.findItem(R.id.action_login).setVisible(false);
			if(hasPermission){
				fetchCodeInfo();
				return super.onCreateOptionsMenu(menu);
			} else {
				menu.findItem(R.id.action_permission).setVisible(true);
			}
		} else {
			menu.findItem(R.id.action_login).setVisible(true);
			menu.findItem(R.id.action_permission).setVisible(false);
		}
		return true;
	}

	LocalBroadcastManager localBroadcastManager;
	LoginReceiver loginReceiver;


	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(null != loginReceiver){
			localBroadcastManager.unregisterReceiver(loginReceiver);
			loginReceiver = null;
		}
	}


	private class LoginReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			invalidateOptionsMenu();
		}
	}
}
