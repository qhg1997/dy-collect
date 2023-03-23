package com.qhg.sgp.util;

import com.qhg.dy.utils.IO;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
public class Test {

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        StringBuilder stringBuilder = new StringBuilder("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">  \n" +
                "<html  xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<body>\n" +
                "    <p></p>\n" +
                "<div style=\"text-align: center;\"><span style=\"font-size: medium; font-family: 宋体;\"><img\n" +
                "            src=\"https://img.9a34b7.com/2023/3/20/c27b153b722f305378e8ebd10c14c2b5.jpg\" style=\"max-width: 100%;\"></img></span>\n" +
                "</div>\n" +
                "<div style=\"text-align: center;\"><span style=\"font-size: medium; font-family: 宋体;\"><iframe class=\"video-iframe\"\n" +
                "            src=\"/#/video?videoid=1162\" frameborder=\"0\" allowfullscreen=\"true\"></iframe></span></div>\n" +
                "<div style=\"text-align: center;\"><span style=\"font-size: medium; font-family: 宋体;\"></span></div><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\"><span style=\"font-weight: bold;\">\n" +
                "        <div style=\"text-align: center;\">文案：鱼虾 丨 旁白：六六 丨 剪辑：小万</div>\n" +
                "    </span><img src=\"https://img.9a34b7.com/2023/3/20/80da7f1de3ea5c7202cd7a47d7d52cb5.png\"\n" +
                "        style=\"max-width:100%;\"></img>美好的大学生活既充满了活力也充斥着各种诱惑，有人立志好好学习将来出人头地，就有人沉迷欲望游戏人生。对于当代大学生来说，最重要的就是能够克制自己，将学习放在首位。当然，要是隔壁住着白石茉莉奈这样的人妻的话，升学毕业什么的还是先放在一边好了！<span\n" +
                "        style=\"font-weight: bold; color: rgb(249, 150, 59);\">番号： JUQ-176片名：沉迷于和人妻性交 我大学留级了</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/2b1258715c1967f69941aa5b543d213b.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">清晨的闹钟响起，小白开启了全新的一天，作为大学生的他不像隔壁只知道吃喝享乐的学长，他的目标明确，就是要毕业之后找个好单位，拿高薪、居高位，然后潜规则漂亮的女下属。</span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/6aaec4a68b2514b3b9506dd2ef6aabf6.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">当然，此时的小白还很单纯，目前的他虽然没人喜欢，但是对待爱情还是充满着幻想。</span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/c951f1dd26eb78b4b7db42560cc6f0e9.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">说爱情，爱情很快就来了，就在这天，小白准备出门，一道靓丽的倩影突然出现在了小白面前，对方是新搬来的住户，名叫小奈，是个人妻。</span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/c2a2ff273a6e5dca8ac78629cc24e5bf.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">尽管知道对方已经结婚，但看到对方甜美的笑容，小白内心的小鹿开始了拼命乱撞。人生第一次，小白感觉自己恋爱了。</span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/93d03b6e82310694e9e7805a48d15003.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">从此，小白对小奈格外上心，看似邻里之间的正常互帮互助，但只要见到小奈，小白就有股说不出的高兴。</span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/60258be33c22711fa4ebb684ec4d2a49.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">夜里，和学长们聊天，小白丝毫不受其扰，拒绝掉他们诱惑的同时，他发现他脑海中挥之不去的诱惑却始终只有隔壁的少妇。</span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/86430e634654156939069543699a37bb.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">又是一天，小白放学回家，刚好遇到小耐在敲他的房门，一问才知道，\n" +
                "    小奈为报答小白之前帮了她的忙，今天特意做了一桌子菜来请小白吃。</span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/f930dbf5ee6ba98e7438a7308b5148d8.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">小白受宠若惊，也没有拒绝，直接跟着这个让自己魂牵梦绕的女人进了屋。</span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/11f6cfa2998f37581d39b44689753463.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">这种状态下，小白哪里是在吃饭，面对他喜欢的小奈，小白是杯子都拿不稳，一不小心就洒了自己一身。</span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/fb468381d49bd3a367b5349a06337837.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">不等小白说抱歉，小奈就热情地把小白脱到只剩内裤，说是怕湿的穿了着凉，但派友们心知肚明：无非是寂寞人妻的惯用伎俩罢了。</span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/de584c99a3f1bfb9173271ace562ef24.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">果不其然，小奈看着小白年轻的身体愈发不能控制住自己的欲望，擦拭身子是假，趁机揩油是真。</span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/14ef2fc894dba38f336176070f2b55b1.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">擦着擦着，小奈的动作越来越大，一把搂住小白后，竟然直接伸手摸向了小白的鸡儿。</span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/f76313a1a638f57717f743cac62bad8f.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">连暧昧的气氛都省略掉，小奈敞亮地将小白最后的内裤防御也给卸下，温柔的小手轻轻裹住肉棒，一边感受着年轻人的火热一边欣赏小伙子羞涩的表情。</span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/05a39f8abbf0d6b272166e1a3c22de44.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">小白哪里想得到人妻竟这般主动，不过自己把柄在对方手上，他也只能是任人宰割。</span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/7223cbff30aaed65f554cfb86cdc60bd.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">言语挑逗一阵，小奈附上口舌，开始了对小白的口交服务。</span>\n" +
                "    <img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/2667880fe43be88639445e3062d2141a.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">只见小奈跪在小白屌前，部位由龟头到蛋蛋，动作由舔到裹，力度由轻到重。不一会儿就给小小白玩吐了。</span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/b83a6071a6c849b33712337a57a418be.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\"></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/deb450cb7e79445070d70ed6a3a1d782.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\"></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/eca113eeca71524a189a88543d5acacd.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">小白是第一次感受到头脑一片空白的快感，自此，他毫无疑问地沉迷在了小奈的温柔乡中。</span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/5e3bd24d75a809aca2123cff62be1368.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">第二天，小奈一早就在门口堵住了去上学的小白。小奈凑到小白耳边说道：上学，上什么学，跟老娘来，教你一些学校学不到的东西！</span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/f743c74e97c1e6f304c676b704086d2f.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">小白不知所以，但是又没办法拒绝风骚的少妇，只能被小奈一把拉进房间。</span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/fdd38e8bf619525e001ce2c0f23cfe5e.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">原本学业为重的少年根本抵挡不住人妻的纠缠，才一个舌吻的工夫，小白就彻底忘记了自己大学生的身份。</span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/7d957e0d089f7045c9ad5aeea7b5b7e1.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">小奈一心只想做爱，把小白放倒在床上后，欺身压上，用自己丰满柔软的身体磨蹭着年轻人欲火中烧的躯体。</span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/e20beedce4964330b1b039f2ccfbc4e8.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">小奈细细为小白舔遍全身，让小白享受了一番帝王般的服务。</span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/de723fc9b6eb6d5d6462a13fea13f615.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-size: medium; font-family: 宋体;\">然后小奈猛攻小白肉棒，分分钟就让小白闭眼呻吟享受到不能自已。</span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/5c5b586afd4a8c2c311e57b0e52bffc6.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span style=\"font-size: medium;\">手撸口舔不够，卖力地裹吸着实让派友们都羡慕不已。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/8c5934d613792b56602d7ba166671ae3.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">口爱到小白再也忍受不住，小奈继续祭出自己胸口的一对大杀器。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/7120ec33ff135617a315c1e059d2bdd2.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">小奈的大奶子杀伤力派友们哪怕没见过也有所耳闻，小白自然难以抵挡，抱起那对巨乳就是一通揉捏舔吸。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/0ababeedc8bf23259ab36de97e8c2066.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">小奈被舔爽了也不甘示弱，掀起小白的菊花就是一顿吸。爽得小白也是直喊舒服！</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/f9c61af14b785335b3efaad7c1cea6e9.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/e45d2e15f0ebd0e3b7d4f8a493ea4b3c.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">脱光之后，派友们看着小奈略显丰腴的身体，不禁有些吃惊：白妈这发福得有点严重啊！</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/1b31afdd355e0086750789121712101f.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">小奈可不在意派友们的看法，一屁股坐上小白面门，将自己最宝贵的汁液献到了小白嘴里。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/1e59882752e0b128d00941a471c96d96.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">感觉到自己下身湿哒哒一片之后，小奈往下挪了挪大屁股，找准肉棒噗嗤一声，接下来就是两人亲密无间的疯狂套弄。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/a640fd51d88346ae076c5d036b8b9ace.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/8086e7f3a24506080068bf7977ff3c91.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">女上位的时候，派友们也没想到小奈如此肥美的身躯竟然还一如当年一般灵活。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/4e778230af3fe5b0f541ded2407362bc.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">转身后入，小白不顾一切地冲撞着小奈缓冲效果拉满的大肉臀。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/31afbb3c88c90f2587d7c8c1cb34e216.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">后入式传教士各高潮一波之后，小白毫无顾忌地内射了小奈一穴。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/df05fd63795c804ccf9753f5270a6de5.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/76c12913ed0b5ead96274a9eaed5bfc4.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/236f061ddd0dd51ec4a66db435c3a6db.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">食髓知味之后，不用说，小白已经彻底被隔壁的少妇征服了去，心软鸡巴硬的小白甚至把家里的钥匙都给了对方一把。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/49bb45b0915b6eea937a35c74fb2fcb0.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">这天，小白刚回来准备继续学习，奈何小奈一脸淫荡的盯着自己，不要说小白了，就算是定力十足的派友们被小奈这样干扰，想要学习是绝对不可能的。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/475803a9c17465399b46191192c7a3d0.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">小白实在没有办法，想跟小奈谈一谈梦想，小奈一听，立马来了性致：你的梦想一时半会也实现不了，不如先来实现我的梦想吧，我的梦想就是立刻马上让你射精！</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/cfd9bc1be97ef0f0e77e43c9e17c703b.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">于是不顾小白想要学习的心思，小奈用自己的指尖和舌尖瞬间就让瓦解了小白学习的念头。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/8939e84b5db2be8b7f64c563a041b62b.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span style=\"font-size: medium;\">抚上奶头再摸上鸡鸡，小白只能任由眼前的少妇摆弄。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/1f1661986c29084c728b18fb2eea4887.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/6544e233c12fd91f09bab7866bbdcb6f.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">分分钟撸到白浆沾湿手指，小奈性致更甚，面对小白询问关于自己老公的事情，小奈让小白只管放宽心：你大哥姓绿，管不住老娘。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/3346800625e05c14b2abc6b33705d3ed.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/70c24753f7746ea959bffe051d983c32.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/f2907506fdce59237a7da8a77202ab1b.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">接下来的几个月里，只要小奈想要了，她就会主动来到小白房间，两人日夜交合，完全忘记了学业。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/62c93339ad33f80c0da9b19db6ade8cd.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">直到学长们找到小白，小白这才得知自己因为翘课数月，挂科留级了。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/c801565caaa014f8a1c3522cb577c90b.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">送走学长之后，小白依旧不能接受这残酷的现实，留级意味着不能毕业，不能毕业可就再无面目回家面对爹娘。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/1bc49dc1241721d688b26d6c79c5d552.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">一时的打击让小白彻底崩溃，小奈看到小白歇斯底里的模样也意识到这段时间是自己耽误了对方，她只能默默回到自己家中，好让小白冷静一下。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/8a07eb8e9680049a73bcfad584285085.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">才一进屋，小奈就被绿哥的突然到访吓了一跳，尽量掩饰着因为出轨而慌乱的内心过后，绿哥也直接表明态度：老子都回来三天了，不见你人。不过也没关系，老子对你的私生活没有兴趣。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/920eaab174078d431112d1718d94c658.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">派友们见到如此冷漠的绿哥，对于小奈出轨这件事也就理解了十之八九了。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/c27634ede698ef47c885e15ab59f5b50.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span style=\"font-size: medium;\">说完这些，绿哥摔门而去，留下一脸心酸的小奈。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/5b7d0e524ea3bc20845066035c15d512.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">小奈内心郁闷，再次敲开了小白家房门，也不管想要通过学习挽回局面的小白，一把吻住对方缠着对方就要发泄自己。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/86912def98199b386a770f7acd1bcb04.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">而小白也确实是个没定力的家伙，轻轻一碰就再一次丢掉了底线。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/8da5aac68ca0cae8946ebd8427f0fa6f.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">两人一边脱衣一边忘情湿吻，不多时小奈的小嘴就含住了小白坚挺的牛子。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/42922a56dc2999a13115467f5ede87f5.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/0b12d844313131e66c73a03c433f5334.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">鸡儿入喉，小奈又一次想起了老公的冷漠，不由得加大了吮吸肉棒的力度。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/2f10ce040c85e79958dcaeb3c4f488ca.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">小白被对方口得心乱如麻，却又情不自禁按倒小奈大肆吮吸起对方的巨大乳房。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/470bc5b6087d662e3dad6670cd7f8f36.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">这对男女一个学业受挫一个感情失意，急需发泄的怒火让两人就如干柴烈火一般一点就着。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/b890019de4cb8b0660c75b4d950dca72.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">湿漉漉的小穴被小白狠狠舔住，淫液救不了火，反而让欲火更旺。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/ded03a6650d6cc891ef771c2785fe470.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">灵巧的手指深抠一阵小穴，率先让小奈迎来了第一波快感。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/ddf765325e71f08204ae4ef9ae5ef71a.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">小奈迫不及待脱干净衣裙，翻身以69姿势和小白互舔下体。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/07545ad2419d4daead8030bcc6fae8cb.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span style=\"font-size: medium;\">口至汗流浃背，两人以传教士合体输出。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/f482369d0c90d8d173dca5a850be92ad.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span style=\"font-size: medium;\">又是一波高潮来临，小奈的下身已然被白浆糊住。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/0c51f1d87dd65fc2abae849931512847.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/0bbb9efe927a022f55264afbbdfaa975.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span style=\"font-size: medium;\">不过操得正动情的两人压根不知道疲惫为何物。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/d500b3ea17f16e56fba3d36646c97cfe.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">女上位、后入式，小奈硕大的奶子跟着节奏甩出动人的弧度。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/85d6c3c8817993dc8b3d56e571f43aeb.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/d20555726b603dfb392c81d64abb7319.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/4bfa96b0029e9ee620aadf3e6e53275e.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span style=\"font-size: medium;\">最后一发内射结束战斗，两个孤独的灵魂相拥良久。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/8cd3b2636dd2b4bb5c8c607173fd007d.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/cfe365713aef5464d5f060b32dcbf25e.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">又过了几个月，小奈已经离婚可以继续和小白鬼混了。而小白也不再需要为学业发愁了，因为他已经因为长时间翘课被学校开除了。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/77db010c6716a12dbc3c07a60f8e851a.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span style=\"font-weight: bold; font-size: x-large;\">总结：</span><span\n" +
                "        style=\"font-size: medium;\">这部JUQ-176，没记错的话咱水果派应该讲过同类型剧情的影片，不过既然是白妈的作品，咱就当是对剧情的复习巩固好了。剧情上看点很简单，就是一个好学少年变成好穴少年的堕落历程。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/3b0eee96e10189ffd45c6e6684adb478.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">造成这样的结果不乏有学长的蛊惑和小白本身的意志不坚定，不过最主要的责任还是在于白妈的勾引，人妻少妇的魅力，果然是任何人都无法抵抗的。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/92e987f36a3a86c6ceaf1d44b7d00828.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">女优白石茉莉奈，看完影片派友们最大的观感想必和我一样：白妈真的胖了好多！不过想来也没什么特别，毕竟是86年的小阿姨，中年发福纯属正常，本片已经很尽力的让白妈尽量少全裸出镜了，哪怕出镜也都尽量让男优挡住她略显肥美的肚腩了。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/88ebb7d8059236ae4c60fbf50b2ddfc1.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span\n" +
                "        style=\"font-size: medium;\">不过好在白妈颜值依旧在线，对着白妈的俏脸，大家酌情撸管就好。</span></span><img\n" +
                "    src=\"https://img.9a34b7.com/2023/3/20/5dc979a680c8ad39faa5b858973bce70.jpg\" style=\"max-width:100%;\"></img><span\n" +
                "    style=\"font-family: 宋体;\"><span style=\"font-size: medium;\">以上就是本期内容，感谢收看！</span><span\n" +
                "        style=\"font-size: medium;\">水果派·看懂av</span><span style=\"font-size: medium;\">我是六六，下期见！</span><span\n" +
                "        style=\"font-size: medium;\">see you~</span></span>\n" +
                "<p></p>\n" +
                "</body>\n" +
                "</html>");
//        String clean = Jsoup.clean(stringBuilder.toString(), Whitelist.basicWithImages());
//        System.out.println(clean);
        String aaa = PDFUtils.html2Pdf(stringBuilder.toString().replace("",""), "aaa");
        System.out.println(aaa);
        log.info("转换结束，耗时：{}ms", System.currentTimeMillis() - startTime);
    }
}