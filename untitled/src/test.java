import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class test {
    public static void main(String[] args) {
        List<Person> personList=new ArrayList<Person>();
        personList.add(new Person("Tom",8900,18,"male","New York"));
        personList.add(new Person("Jack",7000,20,"male","Washington"));
        personList.add(new Person("Lily",7800,23,"female","Washington"));
        personList.add(new Person("Anni",8200,30,"female","New York"));
        personList.add(new Person("Owen",9500,19,"male","New York"));
        personList.add(new Person("Alisa",7900,45,"female","New York"));
//        System.out.println("成功");List<Integer> list = Arrays.asList(7, 6, 9, 3, 8, 2, 1);
//        Integer[] arr = {7, 6, 9, 3, 8, 2, 1};
//        List<Integer> list= Arrays.asList(arr);
//        List<Integer> sums = list.stream().filter(x -> {
//            if (x > 6) {
//                return true;
//            }
//            return false;
//        }).collect(Collectors.toList());
//        sums.stream().forEach(System.out::println);
//        List<String> fiterList=personList.stream().filter(x->x.getSalary()>8000)
//                .map(Person::getArea).collect(Collectors.toList());
//        fiterList.stream().forEach(s -> System.out.println("高于8000的员工姓名："+s));
        List<Person> personListNew = personList.stream().map(person -> {
            Person personNew = new Person(person.getName(), 0, 0, null, null);
            personNew.setSalary(person.getSalary() + 1000);
            return personNew;
        }).collect(Collectors.toList());
        personListNew.forEach(x->{
            System.out.println(x.getSalary()+","+x.getAge());
        });

    }
}
