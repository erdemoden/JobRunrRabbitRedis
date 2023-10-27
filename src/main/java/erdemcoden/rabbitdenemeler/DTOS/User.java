package erdemcoden.rabbitdenemeler.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable{

    private String mail;
    private String name;
    private String surname;
    //private int age;
}
