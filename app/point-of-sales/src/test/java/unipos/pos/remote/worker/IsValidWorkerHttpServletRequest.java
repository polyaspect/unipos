//package unipos.pos.remote.worker;
//
//import org.hamcrest.Description;
//import org.hamcrest.TypeSafeMatcher;
//import org.mockito.ArgumentMatcher;
//import org.springframework.web.util.WebUtils;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * @author ggradnig
// */
//public class IsValidWorkerHttpServletRequest extends TypeSafeMatcher<HttpServletRequest> {
//    @Override
//    public boolean matchesSafely(HttpServletRequest argument) {
//        if(WebUtils.getCookie(argument, "AuthToken") == null) {
//            return false;
//        }
//        return WebUtils.getCookie(argument, "AuthToken").getValue().equals("xxxx-xxxx-xxxx-xxxx");
//    }
//
//    @Override
//    public void describeTo(Description description) {
//
//    }
//}
