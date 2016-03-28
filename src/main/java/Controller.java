public class Controller {
    private CVParser parser;
    private MainView view;

    public Controller(CVParser parser, MainView view){
        this.parser = parser;
        this.view = view;
    }
}
