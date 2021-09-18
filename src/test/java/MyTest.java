import com.study.pojo.Books;
import com.study.service.BookService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author zengyujie
 * @version 1.0
 * @Description: TODO
 * @Date 2021/9/16 16:55
 */
public class MyTest {
    @Test
    public void test(){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        BookService bookServiceImpl = (BookService)context.getBean("BookServiceImpl");

        for (Books books : bookServiceImpl.queryAllBook()) {
            System.out.println(books);
        }
    }

}
