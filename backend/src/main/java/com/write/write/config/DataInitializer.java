package com.write.write.config;

import com.write.write.entity.PracticeBook;
import com.write.write.entity.PracticeLibrary;
import com.write.write.entity.PracticeQuestion;
import com.write.write.entity.PracticeSet;
import com.write.write.entity.SampleEssay;
import com.write.write.repository.PracticeBookRepository;
import com.write.write.repository.PracticeLibraryRepository;
import com.write.write.repository.PracticeQuestionRepository;
import com.write.write.repository.PracticeSetRepository;
import com.write.write.repository.SampleEssayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final SampleEssayRepository sampleEssayRepository;
    private final PracticeLibraryRepository libraryRepo;
    private final PracticeSetRepository setRepo;
    private final PracticeBookRepository bookRepo;
    private final PracticeQuestionRepository questionRepo;

    @Override
    public void run(String... args) {
        // 初始化练习题数据
        initializePracticeData();
        
        // 初始化范文数据
        initializeSampleEssays();
    }
    
    private void initializePracticeData() {
        // ⚠️ 暂时禁用自动初始化题目数据，等待手动插入真实数据
        System.out.println("[DataInitializer] 练习题数据初始化已禁用，请手动插入数据");
    }
    
    private void initializeSampleEssays() {
        // 检查是否已有数据
        if (sampleEssayRepository.count() > 0) {
            System.out.println("[DataInitializer] 数据库中已有范文数据，跳过初始化");
            return;
        }

        System.out.println("[DataInitializer] 开始初始化范文数据...");

        // 范文1：我的同桌
        SampleEssay essay1 = new SampleEssay();
        essay1.setTitle("我的同桌");
        essay1.setAuthorName("张三");
        essay1.setAuthor("张三 三年级");
        essay1.setContent("我的同桌林晓，总穿着亮黄色的外套，像一朵小太阳。她有一双明亮的大眼睛，笑起来的时候，眼睛会弯成月牙。她学习很认真，每次上课都坐得端端正正，认真听老师讲课。\n\n" +
                "林晓不仅学习好，而且很善良。有一次，我忘记带橡皮了，她毫不犹豫地把自己的橡皮借给了我。还有一次，班上的小明摔倒了，她第一个跑过去扶他起来，还帮他拍掉身上的灰尘。\n\n" +
                "我们经常一起做作业，一起玩耍。她总是能想出很多有趣的游戏，让我们的课间时间变得特别快乐。和她做同桌，我感到很幸运。\n\n" +
                "我希望我们能一直做好朋友，一起学习，一起成长。");
        essay1.setTag("写人");
        essay1.setFavoriteCount(233);
        essay1.setCreatedAt(LocalDateTime.now());
        essay1.setUpdatedAt(LocalDateTime.now());

        // 范文2：春天的校园
        SampleEssay essay2 = new SampleEssay();
        essay2.setTitle("春天的校园");
        essay2.setAuthorName("李四");
        essay2.setAuthor("李四 初一");
        essay2.setContent("春天来了，校园里变得生机勃勃。走进校门，首先映入眼帘的是两排高大的梧桐树，它们刚刚抽出嫩绿的新芽，在微风中轻轻摇摆，好像在向我们招手。\n\n" +
                "操场上，小草从泥土里探出头来，绿油油的一片，像给大地铺上了一层绿色的地毯。花坛里的花儿也竞相开放，有粉色的桃花，白色的梨花，还有黄色的迎春花，五颜六色，美丽极了。\n\n" +
                "同学们脱下了厚厚的棉衣，换上了轻薄的春装，在操场上奔跑、嬉戏，欢声笑语充满了整个校园。教室里，阳光透过窗户洒进来，照在课桌上，暖洋洋的。\n\n" +
                "春天的校园真美啊！我爱这充满生机的春天，更爱我们美丽的校园。");
        essay2.setTag("写景");
        essay2.setFavoriteCount(156);
        essay2.setCreatedAt(LocalDateTime.now());
        essay2.setUpdatedAt(LocalDateTime.now());

        // 范文3：难忘的一件事
        SampleEssay essay3 = new SampleEssay();
        essay3.setTitle("难忘的一件事");
        essay3.setAuthorName("王五");
        essay3.setAuthor("王五 四年级");
        essay3.setContent("那是一个阳光明媚的周末，我和爸爸妈妈一起去公园玩。公园里人很多，有放风筝的，有散步的，还有在草地上野餐的。\n\n" +
                "突然，我看到一个小女孩在哭，她的妈妈看起来很着急。我走过去问：\"小妹妹，你怎么了？\"小女孩抽泣着说：\"我的气球飞走了。\"我抬头一看，一个红色的气球正慢慢飘向天空。\n\n" +
                "我安慰她说：\"别哭了，我们一起想办法。\"这时，我看到旁边有一个卖气球的叔叔，我灵机一动，���自己的零花钱买了一个新的气球送给了小女孩。小女孩破涕为笑，她的妈妈也连声道谢。\n\n" +
                "虽然我花掉了自己的零花钱，但看到小女孩开心的笑容，我觉得很值得。这件事让我明白了，帮助别人是一件很快乐的事情。");
        essay3.setTag("叙事");
        essay3.setFavoriteCount(189);
        essay3.setCreatedAt(LocalDateTime.now());
        essay3.setUpdatedAt(LocalDateTime.now());

        // 范文4：我的妈妈
        SampleEssay essay4 = new SampleEssay();
        essay4.setTitle("我的妈妈");
        essay4.setAuthorName("赵六");
        essay4.setAuthor("赵六 二年级");
        essay4.setContent("我的妈妈是一个很温柔的人。她有一头乌黑的长发，总是扎成一个马尾辫。她的眼睛很亮，像星星一样。\n\n" +
                "妈妈每天都很忙，早上要给我做早餐，送我上学，然后去上班。晚上下班回来，还要给我做饭、辅导作业。虽然很累，但她从来没有抱怨过。\n\n" +
                "妈妈很关心我的学习。每当我遇到不会的题目，她总是耐心地给我讲解，直到我明白为止。她还经常给我买书，鼓励我多读书，多学习。\n\n" +
                "妈妈也很关心我的生活。天气冷了，她会提醒我多穿衣服；我生病了，她会整夜不睡地照顾我。\n\n" +
                "我爱我的妈妈，她是我心中最伟大的人。我要好好学习，长大后好好孝顺她。");
        essay4.setTag("写人");
        essay4.setFavoriteCount(267);
        essay4.setCreatedAt(LocalDateTime.now());
        essay4.setUpdatedAt(LocalDateTime.now());

        // 范文5：美丽的秋天
        SampleEssay essay5 = new SampleEssay();
        essay5.setTitle("美丽的秋天");
        essay5.setAuthorName("孙七");
        essay5.setAuthor("孙七 五年级");
        essay5.setContent("秋天来了，天气渐渐变凉了。树叶开始变黄，一片片从树上飘落下来，像一只只黄色的蝴蝶在空中飞舞。\n\n" +
                "田野里，稻子成熟了，金黄金黄的，像一片金色的海洋。农民伯伯们正在忙着收割，脸上洋溢着丰收的喜悦。\n\n" +
                "果园里，苹果红了，梨子黄了，葡萄紫了，各种水果都成熟了，散发着诱人的香味。我和小伙伴们一起去摘果子，一边摘一边吃，可开心了。\n\n" +
                "公园里，菊花开了，有黄的、白的、紫的，五颜六色，美丽极了。人们纷纷来到公园，欣赏这美丽的秋景。\n\n" +
                "秋天真是一个美丽的季节，我爱秋天！");
        essay5.setTag("写景");
        essay5.setFavoriteCount(145);
        essay5.setCreatedAt(LocalDateTime.now());
        essay5.setUpdatedAt(LocalDateTime.now());

        // 范文6：第一次做饭
        SampleEssay essay6 = new SampleEssay();
        essay6.setTitle("第一次做饭");
        essay6.setAuthorName("周八");
        essay6.setAuthor("周八 六年级");
        essay6.setContent("今天，爸爸妈妈都去上班了，只有我一个人在家。到了中午，我的肚子饿得咕咕叫，我想：不如我自己做一次饭吧！\n\n" +
                "我打开冰箱，看到有鸡蛋、西红柿和面条。我决定做西红柿鸡蛋面。首先，我把西红柿洗干净，切成小块。然后，我打了两个鸡蛋，搅拌均匀。\n\n" +
                "接下来，我打开煤气灶，往锅里倒了一些油。等油热了，我把鸡蛋倒进去，炒成金黄色，盛出来。再把西红柿倒进去炒，等西红柿出汁了，我把炒好的鸡蛋倒进去，一起炒。\n\n" +
                "最后，我往锅里加了水，等水开了，我把面条放进去。煮了几分钟，面条就熟了。我尝了一口，虽然味道没有妈妈做的好，但这是我第一次做饭，我觉得很有成就感。\n\n" +
                "通过这次做饭，我体会到了妈妈的辛苦，以后我要多帮妈妈做家务。");
        essay6.setTag("叙事");
        essay6.setFavoriteCount(178);
        essay6.setCreatedAt(LocalDateTime.now());
        essay6.setUpdatedAt(LocalDateTime.now());

        // 保存所有范文
        sampleEssayRepository.save(essay1);
        sampleEssayRepository.save(essay2);
        sampleEssayRepository.save(essay3);
        sampleEssayRepository.save(essay4);
        sampleEssayRepository.save(essay5);
        sampleEssayRepository.save(essay6);

        System.out.println("[DataInitializer] 成功初始化 6 篇范文数据");
    }
}
