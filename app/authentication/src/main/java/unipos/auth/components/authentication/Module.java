package unipos.auth.components.authentication;

import lombok.Data;

/**
 * Created by Dominik on 26.07.2015.
 */

@Data
public class Module {

    private String name;
    private String adresse;

    public Module() {}

    public Module(String name) {
        this.name = name;
    }

    public Module(String name, String adresse) {
        this.name = name;
        this.adresse = adresse;
    }

}
