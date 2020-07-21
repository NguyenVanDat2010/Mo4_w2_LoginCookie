package demo.controller;

import demo.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
//Annotation @SessionAttributes("user") được sử dụng để lưu trữ thông tin của model attribute có tên là user.
@SessionAttributes("user")
public class LoginController {

    //Đoạn mã sau đây sẽ add User vào trong model attribute:
    /*add user in model attribute*/
    @ModelAttribute("user")
    public User setUpUserForm() {
        return new User();
    }

    @RequestMapping("/login")
    public String index(@CookieValue(value = "setUser", defaultValue = "") String setUser, Model model) {
        Cookie cookie = new Cookie("setUser", setUser);
        model.addAttribute("cookieValue", cookie);
        return "login";
    }

    //Annotation @ModelAttribute("user") sẽ nhận user trả về từ view, sau đó đưa vào session
    //Annotation @CookieValue để ràng buộc giá trị của cookie HTTP với tham số phương thức trong controller.
    @PostMapping("dologin")
    public String dologin(@ModelAttribute("user") User user,
                          @CookieValue(value = "setUser", defaultValue = "") String setUser,
                          HttpServletResponse response, HttpServletRequest request, Model model) {
        //implement business logic
        if (user.getEmail().equals("admin@gmail.com") && user.getPassword().equals("123456")) {
            if (user.getEmail() != null) {
                /** Gán email truyền vào cho biến setUser
                 * Tạo cookie và trả về cho client */
                setUser = user.getEmail();

                // create cookie and set it in response
                Cookie cookie = new Cookie("setUser", setUser);
                //cookie sẽ được lưu trong trình duyệt bao lâu
                cookie.setMaxAge(24 * 60 * 60);
                //response sẽ trả cookie về cho view sử dụng phương thức: response.addCookie(cookie);
                response.addCookie(cookie);

                /**duyệt danh sách cookie và lấy cookie có tên 'setUser' sau đó truyền vào model.*/
                //get all cookies
                Cookie[] cookies = request.getCookies();
                //iterate each cookie: lặp cookie
                for (Cookie ck : cookies) {
                    //display only the cookie with the name 'setUser'
                    if (!ck.getName().equals("setUser")) {
                        ck.setValue("");
                    }
                    model.addAttribute("cookieValue", ck);
                    break;
                }
                model.addAttribute("message", "Login success. Welcome");
            }
        } else {
            user.setEmail("");
            Cookie cookie = new Cookie("setUser", setUser);
            model.addAttribute("cookieValue", cookie);
            model.addAttribute("message", "Login failed. Try again.");
        }
        return "login";
    }


}
