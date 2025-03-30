class Vehicle{
    void VehicleDetails(){
        System.out.println("The Vehicle Details...");
    }
}
class Car extends Vehicle{
    void CarDetails(){
        System.out.println("The Car Details...");
    }
}
class Sports extends Car{
    void SportsDetails(){
        System.out.println("The Sports Details...");
    }
}
public class Transport{
    public static void main(String args[]){
        Sports s=new Sports();
        s.VehicleDetails();
        s.CarDetails();
        s.SportsDetails();
    }
}