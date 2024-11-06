package com.securityModel.Dtos.response;


import com.securityModel.Dtos.request.DriverRequest;
import com.securityModel.models.Driver;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponse {
    private String adress;



    public static DriverResponse fromEntity(Driver entity){
        DriverResponse driverResponse=new DriverResponse();
        BeanUtils.copyProperties(entity,driverResponse);
        return driverResponse;
    }

    public static Driver toEntity(DriverRequest driverResponse){
        Driver d=new Driver();
        BeanUtils.copyProperties(driverResponse,d);
        return d;
    }

}
