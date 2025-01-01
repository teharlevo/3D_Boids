package main;

public abstract class Component {
    
    private GameObject gameObject;

    public abstract void load();

    public abstract void init();

    public abstract void update();

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public GameObject getGameObject() {
        return gameObject;
    }
}
