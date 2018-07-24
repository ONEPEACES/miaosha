import com.imooc.miaosha.MainApplication;
import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.rabbitmq.MQSender;
import com.imooc.miaosha.rabbitmq.MiaoshaMsg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *@author Xue
 *@date 2018/7/13 14:15
 *@description  SpringBootTest需要主运行函数的环境即@SpringBootTest(classes = MainApplication.class)
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class MainApplicationTest {

    @Autowired
    private MQSender mqSender;

    @Test
    public void rabbitMqTest(){
        int a = 0;
        mqSender.sendToMsg(new MiaoshaMsg(new MiaoshaUser(),1));
    }
}
