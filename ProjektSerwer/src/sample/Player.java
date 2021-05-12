package sample;

public class Player {

    private String name;
    private String status;
    private PlayerConnector playerConnector;


    public Player(String name, String status, PlayerConnector playerConnector ){
        this.name = name;
        this.status = status;
        this.playerConnector = playerConnector;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PlayerConnector getPlayerConnector() {
        return playerConnector;
    }

    public void setPlayerConnector(PlayerConnector playerConnector) {
        this.playerConnector = playerConnector;
    }
}
