package com.mionix.baseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.mionix.baseapp.model.local.Preferences
import com.mionix.baseapp.ui.base.BaseActivity
import com.mionix.baseapp.ui.base.BaseWebView
import com.mionix.baseapp.viewmodel.LovePercentViewmodel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {
    private val mPreferences by inject<Preferences>()
    private val mLovePercentViewmodel : LovePercentViewmodel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mLovePercentViewmodel.getPercentage("Duy","My")
        mLovePercentViewmodel.percentage.observe(this, Observer {
            Toast.makeText(this,it.percentage.toString(),Toast.LENGTH_SHORT).show()
        })
//        val mainWebView = BaseWebView()
//        val a = "<!-- guidanceレイアウト用CSSの読み込み / -->\n" +
//                "        <link rel=\"stylesheet\" type=\"text/css\" href=\"https://ssl.at-s.com/shinsotsu/2021/css/guidance_layout.css\" media=\"all\" />\n" +
//                "\n" +
//                "        <!-- guidance / -->\n" +
//                "        <div class=\"editGuidance\">\n" +
//                "    <p class=\"editGuidanceTitleButton\"><a href=\"#table_kigyo_list_up\" data-ajax=\"false\"><img src=\"https://ssl.at-s.com/shinsotsu/2021/images/guidance/guidence_button_list.png\" alt=\"参加企業一覧\" /></a></p>\n" +
//                "    <p class=\"editGuidanceImagePC\"><img src=\"https://ssl.at-s.com/shinsotsu/2021/images/guidance/20200704shizuoka/index_new_pc_01.png\" alt=\"\" /></p>\n" +
//                "    <p class=\"editGuidanceImageSP\"><img src=\"https://ssl.at-s.com/shinsotsu/2021/images/guidance/20200704shizuoka/index_new_sp_01.png\" alt=\"\" /></p>\n" +
//                "    </div>\n" +
//                "        <!-- / guidance -->\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "        <!-- guidance / -->\n" +
//                "        <div class=\"editGuidance editGuidance009CDC\">\n" +
//                "    <p class=\"editGuidanceImagePC\"><img src=\"https://ssl.at-s.com/shinsotsu/2021/images/guidance/20200704shizuoka/index_new_pc_02.png\" alt=\"\" /></p>\n" +
//                "    <p class=\"editGuidanceImageSP\"><img src=\"https://ssl.at-s.com/shinsotsu/2021/images/guidance/20200704shizuoka/index_new_sp_02.png\" alt=\"\" /></p>\n" +
//                "    </div>\n" +
//                "        <!-- / guidance -->\n" +
//                "\n" +
//                "        <!-- guidance / -->\n" +
//                "        <div class=\"editGuidance editGuidance009CDC\">\n" +
//                "    <p class=\"editGuidanceImagePC\"><img src=\"https://ssl.at-s.com/shinsotsu/2021/images/guidance/20200704shizuoka/index_new_pc_03.png\" alt=\"\" /></p>\n" +
//                "    <p class=\"editGuidanceImageSP\"><img src=\"https://ssl.at-s.com/shinsotsu/2021/images/guidance/20200704shizuoka/index_new_sp_03.png\" alt=\"\" /></p>\n" +
//                "    </div>\n" +
//                "        <!-- / guidance -->\n" +
//                "\n" +
//                "        <!-- guidance / -->\n" +
//                "        <div class=\"editGuidance editGuidanceSSeminer editGuidanceSSeminer009CDC\">\n" +
//                "    <p class=\"editGuidanceSSeminerTitle colorFFFFFF\">☆ 新卒のかんづめ2021に会員登録するとスマホでラクラク入場ができます！ ☆</p>\n" +
//                "    <p class=\"colorFFFFFF\">※受付時に、係員に就活イベント受付QRコードを表示したスマホ画面をお見せください。</p>\n" +
//                "    <p class=\"colorFFFFFF\">就活イベント受付QRコードはマイボードより表示できます。</p>\n" +
//                "    <p class=\"colorFFFFFF\">【QRコード表示の手順】</p>\n" +
//                "    <p class=\"colorFFFFFF\">スマホでマイボードにログイン → プロフィール → ページ下部の就活イベントQRコード「QRコードを表示」をタップ</p>\n" +
//                "    <p><a href=\"https://ssl.at-s.com/shinsotsu/2021/smph_gakusei/sgakLogin.aspx?r=sgak05720\" class=\"editGuidanceSSeminerLink\">QRコードの表示はこちらから</a></p>\n" +
//                "    <p class=\"colorFFFFFF\">※QRコードをご利用いただけない場合は、当日会場にて就活イベント受付カードへのご記入をお願いいたします。</p>\n" +
//                "    </div>\n" +
//                "        <!-- / guidance -->\n" +
//                "\n" +
//                "        <!-- guidance / -->\n" +
//                "        <div class=\"editGuidance editGuidance009CDC\">\n" +
//                "    <ul class=\"guidanceContentsItemButton\">\n" +
//                "    <li><a href=\"https://ssl.at-s.com/shinsotsu/2021/images/guidance/guidance_qr.pdf\" target=\"_blank\"><img src=\"https://ssl.at-s.com/shinsotsu/2021/images/guidance/guidence_button_item_03.png\" alt=\"受付QRコード表示方法\" /></a></li>\n" +
//                "    <li><a href=\"https://ssl.at-s.com/shinsotsu/2021/images/guidance/guidance_sankahouhou.pdf\" target=\"_blank\"><img src=\"https://ssl.at-s.com/shinsotsu/2021/images/guidance/guidence_button_item_04.png\" alt=\"企業ブース訪問方法\" /></a></li>\n" +
//                "    </ul>\n" +
//                "    </div>\n" +
//                "        <!-- / guidance -->\n" +
//                "\n" +
//                "        <!-- guidance / -->\n" +
//                "        <div class=\"editGuidance editGuidanceFFFFFF\">\n" +
//                "    <p class=\"editGuidanceImagePC\"><img src=\"https://ssl.at-s.com/shinsotsu/2021/images/guidance/20200704shizuoka/index_new_pc_04.png\" alt=\"\" /></p>\n" +
//                "    <p class=\"editGuidanceImageSP\"><img src=\"https://ssl.at-s.com/shinsotsu/2021/images/guidance/20200704shizuoka/index_new_sp_04.png\" alt=\"\" /></p>\n" +
//                "    </div>\n" +
//                "        <!-- / guidance -->"
//        mainWebView.setupWebView(a,webView)
    }

    override fun onClickActionLeftListener() {

    }

    override fun setTitleToolbar(): String? {
        return "Base"
    }
}
