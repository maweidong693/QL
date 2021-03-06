package com.weiwu.ql.main.contact.chat.adapter;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/27 10:22 
 */
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

class EmojiAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> emojis;
    public EmojiAdapter(Context context, ArrayList<String> emojis){
        mContext = context;
        this.emojis = emojis;
    }
    public int getCount() {
        return emojis.size();
    }
    public Object getItem(int position) {
        return emojis.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv_emoji = null;
        if (convertView == null) {
            tv_emoji = new TextView(mContext);
            tv_emoji.setTextSize(25);
            convertView = tv_emoji;
            convertView.setTag(tv_emoji);
            tv_emoji.setGravity(Gravity.CENTER);
        } else {
            tv_emoji = (TextView) convertView.getTag();
        }
        tv_emoji.setText(emojis.get(position));
        return convertView;
    }
    public static String parseUnifiedCode(String unifiedCode) {
        return new String(Character.toChars(Integer.parseInt(unifiedCode.replace("U+", ""), 16)));
    }
}

class EmojiProvider{
    public final static ArrayList<String> emojis = new ArrayList<String>(){
        {
            add("😁");
            add("😂");
            add("😃");
            add("😄");
            add("😅");
            add("😆");
            add("😉");
            add("😊");
            add("😋");
            add("😌");
            add("😍");
            add("😏");
            add("😒");
            add("😓");
            add("😔");
            add("😖");
            add("😘");
            add("😚");
            add("😜");
            add("😝");
            add("😞");
            add("😠");
            add("😡");
            add("😢");
            add("😣");
            add("😤");
            add("😥");
            add("😨");
            add("😩");
            add("😪");
            add("😫");
            add("😭");
            add("😰");
            add("😱");
            add("😲");
            add("😳");
            add("😵");
            add("😷");
            add("😸");
            add("😹");
            add("😺");
            add("😻");
            add("😼");
            add("😽");
            add("😾");
            add("😿");
            add("🙀");
            add("🙅");
            add("🙆");
            add("🙇");
            add("🙈");
            add("🙉");
            add("🙊");
            add("🙋");
            add("🙌");
            add("🙍");
            add("🙎");
            add("🙏");
            add("✂");
            add("✅");
            add("✈");
            add("✉");
            add("✊");
            add("✋");
            add("✌");
            add("✏");
            add("✒");
            add("✔");
            add("✖");
            add("✨");
            add("✳");
            add("✴");
            add("❄");
            add("❇");
            add("❌");
            add("❎");
            add("❓");
            add("❔");
            add("❕");
            add("❗");
            add("❤");
            add("➕");
            add("➖");
            add("➗");
            add("➡");
            add("➰");
            add("🚀");
            add("🚃");
            add("🚄");
            add("🚅");
            add("🚇");
            add("🚉");
            add("🚌");
            add("🚏");
            add("🚑");
            add("🚒");
            add("🚓");
            add("🚕");
            add("🚗");
            add("🚙");
            add("🚚");
            add("🚢");
            add("🚤");
            add("🚥");
            add("🚧");
            add("🚨");
            add("🚩");
            add("🚪");
            add("🚫");
            add("🚬");
            add("🚭");
            add("🚲");
            add("🚶");
            add("🚹");
            add("🚺");
            add("🚻");
            add("🚼");
            add("🚽");
            add("🚾");
            add("🛀");
            add("Ⓜ");
            add("🅰");
            add("🅱");
            add("🅾");
            add("🅿");
            add("🆎");
            add("🆑");
            add("🆒");
            add("🆓");
            add("🆔");
            add("🆕");
            add("🆖");
            add("🆗");
            add("🆘");
            add("🆙");
            add("🆚");
            add("🇩🇪");
            add("🇬🇧");
            add("🇨🇳");
            add("🇯🇵");
            add("🇫🇷");
            add("🇰🇷");
            add("🇪🇸");
            add("🇮🇹");
            add("🇷🇺");
            add("🇺🇸");
            add("🈁");
            add("🈂");
            add("🈚");
            add("🈯");
            add("🈲");
            add("🈳");
            add("🈴");
            add("🈵");
            add("🈶");
            add("🈷");
            add("🈸");
            add("🈹");
            add("🈺");
            add("🉐");
            add("🉑");
            add("©");
            add("®");
            add("‼");
            add("⁉");
            add("#⃣");
            add("8⃣");
            add("9⃣");
            add("7⃣");
            add("0⃣");
            add("6⃣");
            add("5⃣");
            add("4⃣");
            add("3⃣");
            add("2⃣");
            add("1⃣");
            add("™");
            add("ℹ");
            add("↔");
            add("↕");
            add("↖");
            add("↗");
            add("↘");
            add("↙");
            add("↩");
            add("↪");
            add("⌚");
            add("⌛");
            add("⏩");
            add("⏪");
            add("⏫");
            add("⏬");
            add("⏰");
            add("⏳");
            add("▪");
            add("▫");
            add("▶");
            add("◀");
            add("◻");
            add("◼");
            add("◽");
            add("◾");
            add("☀");
            add("☁");
            add("☎");
            add("☑");
            add("☔");
            add("☕");
            add("☝");
            add("☺");
            add("♈");
            add("♉");
            add("♊");
            add("♋");
            add("♌");
            add("♍");
            add("♎");
            add("♏");
            add("♐");
            add("♑");
            add("♒");
            add("♓");
            add("♠");
            add("♣");
            add("♥");
            add("♦");
            add("♨");
            add("♻");
            add("♿");
            add("⚓");
            add("⚠");
            add("⚡");
            add("⚪");
            add("⚫");
            add("⚽");
            add("⚾");
            add("⛄");
            add("⛅");
            add("⛎");
            add("⛔");
            add("⛪");
            add("⛲");
            add("⛳");
            add("⛵");
            add("⛺");
            add("⛽");
            add("⤴");
            add("⤵");
            add("⬅");
            add("⬆");
            add("⬇");
            add("⬛");
            add("⬜");
            add("⭐");
            add("⭕");
            add("〰");
            add("〽");
            add("㊗");
            add("㊙");
            add("🀄");
            add("🃏");
            add("🌀");
            add("🌁");
            add("🌂");
            add("🌃");
            add("🌄");
            add("🌅");
            add("🌆");
            add("🌇");
            add("🌈");
            add("🌉");
            add("🌊");
            add("🌋");
            add("🌌");
            add("🌏");
            add("🌑");
            add("🌓");
            add("🌔");
            add("🌕");
            add("🌙");
            add("🌛");
            add("🌟");
            add("🌠");
            add("🌰");
            add("🌱");
            add("🌴");
            add("🌵");
            add("🌷");
            add("🌸");
            add("🌹");
            add("🌺");
            add("🌻");
            add("🌼");
            add("🌽");
            add("🌾");
            add("🌿");
            add("🍀");
            add("🍁");
            add("🍂");
            add("🍃");
            add("🍄");
            add("🍅");
            add("🍆");
            add("🍇");
            add("🍈");
            add("🍉");
            add("🍊");
            add("🍌");
            add("🍍");
            add("🍎");
            add("🍏");
            add("🍑");
            add("🍒");
            add("🍓");
            add("🍔");
            add("🍕");
            add("🍖");
            add("🍗");
            add("🍘");
            add("🍙");
            add("🍚");
            add("🍛");
            add("🍜");
            add("🍝");
            add("🍞");
            add("🍟");
            add("🍠");
            add("🍡");
            add("🍢");
            add("🍣");
            add("🍤");
            add("🍥");
            add("🍦");
            add("🍧");
            add("🍨");
            add("🍩");
            add("🍪");
            add("🍫");
            add("🍬");
            add("🍭");
            add("🍮");
            add("🍯");
            add("🍰");
            add("🍱");
            add("🍲");
            add("🍳");
            add("🍴");
            add("🍵");
            add("🍶");
            add("🍷");
            add("🍸");
            add("🍹");
            add("🍺");
            add("🍻");
            add("🎀");
            add("🎁");
            add("🎂");
            add("🎃");
            add("🎄");
            add("🎅");
            add("🎆");
            add("🎇");
            add("🎈");
            add("🎉");
            add("🎊");
            add("🎋");
            add("🎌");
            add("🎍");
            add("🎎");
            add("🎏");
            add("🎐");
            add("🎑");
            add("🎒");
            add("🎓");
            add("🎠");
            add("🎡");
            add("🎢");
            add("🎣");
            add("🎤");
            add("🎥");
            add("🎦");
            add("🎧");
            add("🎨");
            add("🎩");
            add("🎪");
            add("🎫");
            add("🎬");
            add("🎭");
            add("🎮");
            add("🎯");
            add("🎰");
            add("🎱");
            add("🎲");
            add("🎳");
            add("🎴");
            add("🎵");
            add("🎶");
            add("🎷");
            add("🎸");
            add("🎹");
            add("🎺");
            add("🎻");
            add("🎼");
            add("🎽");
            add("🎾");
            add("🎿");
            add("🏀");
            add("🏁");
            add("🏂");
            add("🏃");
            add("🏄");
            add("🏆");
            add("🏈");
            add("🏊");
            add("🏠");
            add("🏡");
            add("🏢");
            add("🏣");
            add("🏥");
            add("🏦");
            add("🏧");
            add("🏨");
            add("🏩");
            add("🏪");
            add("🏫");
            add("🏬");
            add("🏭");
            add("🏮");
            add("🏯");
            add("🏰");
            add("🐌");
            add("🐍");
            add("🐎");
            add("🐑");
            add("🐒");
            add("🐔");
            add("🐗");
            add("🐘");
            add("🐙");
            add("🐚");
            add("🐛");
            add("🐜");
            add("🐝");
            add("🐞");
            add("🐟");
            add("🐠");
            add("🐡");
            add("🐢");
            add("🐣");
            add("🐤");
            add("🐥");
            add("🐦");
            add("🐧");
            add("🐨");
            add("🐩");
            add("🐫");
            add("🐬");
            add("🐭");
            add("🐮");
            add("🐯");
            add("🐰");
            add("🐱");
            add("🐲");
            add("🐳");
            add("🐴");
            add("🐵");
            add("🐶");
            add("🐷");
            add("🐸");
            add("🐹");
            add("🐺");
            add("🐻");
            add("🐼");
            add("🐽");
            add("🐾");
            add("👀");
            add("👂");
            add("👃");
            add("👄");
            add("👅");
            add("👆");
            add("👇");
            add("👈");
            add("👉");
            add("👊");
            add("👋");
            add("👌");
            add("👍");
            add("👎");
            add("👏");
            add("👐");
            add("👑");
            add("👒");
            add("👓");
            add("👔");
            add("👕");
            add("👖");
            add("👗");
            add("👘");
            add("👙");
            add("👚");
            add("👛");
            add("👜");
            add("👝");
            add("👞");
            add("👟");
            add("👠");
            add("👡");
            add("👢");
            add("👣");
            add("👤");
            add("👦");
            add("👧");
            add("👨");
            add("👩");
            add("👪");
            add("👫");
            add("👮");
            add("👯");
            add("👰");
            add("👱");
            add("👲");
            add("👳");
            add("👴");
            add("👵");
            add("👶");
            add("👷");
            add("👸");
            add("👹");
            add("👺");
            add("👻");
            add("👼");
            add("👽");
            add("👾");
            add("👿");
            add("💀");
            add("💁");
            add("💂");
            add("💃");
            add("💄");
            add("💅");
            add("💆");
            add("💇");
            add("💈");
            add("💉");
            add("💊");
            add("💋");
            add("💌");
            add("💍");
            add("💎");
            add("💏");
            add("💐");
            add("💑");
            add("💒");
            add("💓");
            add("💔");
            add("💕");
            add("💖");
            add("💗");
            add("💘");
            add("💙");
            add("💚");
            add("💛");
            add("💜");
            add("💝");
            add("💞");
            add("💟");
            add("💠");
            add("💡");
            add("💢");
            add("💣");
            add("💤");
            add("💥");
            add("💦");
            add("💧");
            add("💨");
            add("💩");
            add("💪");
            add("💫");
            add("💬");
            add("💮");
            add("💯");
            add("💰");
            add("💱");
            add("💲");
            add("💳");
            add("💴");
            add("💵");
            add("💸");
            add("💹");
            add("💺");
            add("💻");
            add("💼");
            add("💽");
            add("💾");
            add("💿");
            add("📀");
            add("📁");
            add("📂");
            add("📃");
            add("📄");
            add("📅");
            add("📆");
            add("📇");
            add("📈");
            add("📉");
            add("📊");
            add("📋");
            add("📌");
            add("📍");
            add("📎");
            add("📏");
            add("📐");
            add("📑");
            add("📒");
            add("📓");
            add("📔");
            add("📕");
            add("📖");
            add("📗");
            add("📘");
            add("📙");
            add("📚");
            add("📛");
            add("📜");
            add("📝");
            add("📞");
            add("📟");
            add("📠");
            add("📡");
            add("📢");
            add("📣");
            add("📤");
            add("📥");
            add("📦");
            add("📧");
            add("📨");
            add("📩");
            add("📪");
            add("📫");
            add("📮");
            add("📰");
            add("📱");
            add("📲");
            add("📳");
            add("📴");
            add("📶");
            add("📷");
            add("📹");
            add("📺");
            add("📻");
            add("📼");
            add("🔃");
            add("🔊");
            add("🔋");
            add("🔌");
            add("🔍");
            add("🔎");
            add("🔏");
            add("🔐");
            add("🔑");
            add("🔒");
            add("🔓");
            add("🔔");
            add("🔖");
            add("🔗");
            add("🔘");
            add("🔙");
            add("🔚");
            add("🔛");
            add("🔜");
            add("🔝");
            add("🔞");
            add("🔟");
            add("🔠");
            add("🔡");
            add("🔢");
            add("🔣");
            add("🔤");
            add("🔥");
            add("🔦");
            add("🔧");
            add("🔨");
            add("🔩");
            add("🔪");
            add("🔫");
            add("🔮");
            add("🔯");
            add("🔰");
            add("🔱");
            add("🔲");
            add("🔳");
            add("🔴");
            add("🔵");
            add("🔶");
            add("🔷");
            add("🔸");
            add("🔹");
            add("🔺");
            add("🔻");
            add("🔼");
            add("🔽");
            add("🕐");
            add("🕑");
            add("🕒");
            add("🕓");
            add("🕔");
            add("🕕");
            add("🕖");
            add("🕗");
            add("🕘");
            add("🕙");
            add("🕚");
            add("🕛");
            add("🗻");
            add("🗼");
            add("🗽");
            add("🗾");
            add("🗿");
            add("😀");
            add("😇");
            add("😈");
            add("😎");
            add("😐");
            add("😑");
            add("😕");
            add("😗");
            add("😙");
            add("😛");
            add("😟");
            add("😦");
            add("😧");
            add("😬");
            add("😮");
            add("😯");
            add("😴");
            add("😶");
            add("🚁");
            add("🚂");
            add("🚆");
            add("🚈");
            add("🚊");
            add("🚍");
            add("🚎");
            add("🚐");
            add("🚔");
            add("🚖");
            add("🚘");
            add("🚛");
            add("🚜");
            add("🚝");
            add("🚞");
            add("🚟");
            add("🚠");
            add("🚡");
            add("🚣");
            add("🚦");
            add("🚮");
            add("🚯");
            add("🚰");
            add("🚱");
            add("🚳");
            add("🚴");
            add("🚵");
            add("🚷");
            add("🚸");
            add("🚿");
            add("🛁");
            add("🛂");
            add("🛃");
            add("🛄");
            add("🛅");
            add("🌍");
            add("🌎");
            add("🌐");
            add("🌒");
            add("🌖");
            add("🌗");
            add("🌘");
            add("🌚");
            add("🌜");
            add("🌝");
            add("🌞");
            add("🌲");
            add("🌳");
            add("🍋");
            add("🍐");
            add("🍼");
            add("🏇");
            add("🏉");
            add("🏤");
            add("🐀");
            add("🐁");
            add("🐂");
            add("🐃");
            add("🐄");
            add("🐅");
            add("🐆");
            add("🐇");
            add("🐈");
            add("🐉");
            add("🐊");
            add("🐋");
            add("🐏");
            add("🐐");
            add("🐓");
            add("🐕");
            add("🐖");
            add("🐪");
            add("👥");
            add("👬");
            add("👭");
            add("💭");
            add("💶");
            add("💷");
            add("📬");
            add("📭");
            add("📯");
            add("📵");
            add("🔀");
            add("🔁");
            add("🔂");
            add("🔄");
            add("🔅");
            add("🔆");
            add("🔇");
            add("🔉");
            add("🔕");
            add("🔬");
            add("🔭");
            add("🕜");
            add("🕝");
            add("🕞");
            add("🕟");
            add("🕠");
            add("🕡");
            add("🕢");
            add("🕣");
            add("🕤");
            add("🕥");
            add("🕦");
            add("🕧");
        }
    };
}
