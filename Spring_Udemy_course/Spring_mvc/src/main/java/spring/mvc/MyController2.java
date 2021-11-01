package spring.mvc;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.validation.Valid;


@Controller
@RequestMapping("/employee2")
public class MyController2 {

    @RequestMapping("/")
    public String showFirstView(){
        return "first-view";
    }

    @RequestMapping("/askDetails")
    public String askEmployeeDetails(Model model){

        model.addAttribute("employee", new Employee());

        return "ask-emp-details-view2";
    }


    @RequestMapping("/showDetails")
    public String showEmpDetails(@Valid @ModelAttribute("employee") Employee employee
            , BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "ask-emp-details-view2";
        }
        else {
            employee.setSalary(employee.getSalary() * 10);
            return "show-emp-details-view2";
        }
    }
}
