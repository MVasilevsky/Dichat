package chyatus.mvc;

import chyatus.users.User;

/**
 * MVC Controller
 *
 * @author mvas
 */
public class Controller {

    public Model model;

    public Controller(Model model) {
        this.model = model;
    }

    public void test() {
        model.addUser(new User("ololo"));
    }

}
