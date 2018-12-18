package test;

import java.util.Optional;

/**
 * Created by 18435 on 2018/5/8.
 */
public class PersonFactory {
    private PeoPleType peoPleType;

    public PersonFactory(PeoPleType peoPleType){
        this.peoPleType = peoPleType;
    }

    public Optional<Person> getInstance(){
        Person person = null;
        if(this.peoPleType == PeoPleType.man ){
            person = new Man();
        }else if(this.peoPleType == peoPleType.woman){
            person = new Woman();
        }

        if(person ==null){
            return Optional.empty();
        }else{
            return Optional.of(person);
        }
    }
}
