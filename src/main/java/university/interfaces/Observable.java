package university.interfaces;

public interface Observable {
    void subscribe(Subscriber s);
    void unsubscribe(Subscriber s);
    void notifySubscribers(university.models.ResearchPaper paper);
}
