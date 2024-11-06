package com.securityModel.services.impl;


import com.securityModel.Dtos.request.DriverRequest;
import com.securityModel.Dtos.response.DriverResponse;
import com.securityModel.models.Driver;
import com.securityModel.repository.DriverDao;
import com.securityModel.services.DriverService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class DriverServiceIMPL implements DriverService {


    private final DriverDao driverDaoconst;

    public DriverServiceIMPL(DriverDao driverDaoconst) {
        this.driverDaoconst = driverDaoconst;
    }

    @Override
    public DriverResponse createdriver(DriverRequest driver) {
        Driver d= DriverResponse.toEntity(driver);
        Driver saveddriver=driverDaoconst.save(d);
        return DriverResponse.fromEntity(saveddriver);
    }

    @Override
    public List<DriverResponse> alldriver() {
        return driverDaoconst.findAll().stream()
                .map(DriverResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public DriverResponse driverById(Long id) {
        return driverDaoconst.findById(id)
                .map(DriverResponse::fromEntity)
                .orElseThrow(()->new RuntimeException("driver not found with this id"));
    }


    @Override
    public DriverResponse updatedriver(DriverRequest driverRequest, Long id) {
        Driver driver =driverDaoconst.findById(id).orElseThrow(() ->
                new RuntimeException("driver not found with this id:"+id));
        if(driver !=null){
            Driver d=DriverResponse.toEntity(driverRequest);
            d.setId(id);
            d.setAdress(d.getAdress()==null ?driver.getAdress():d.getAdress());
            Driver saveddriver=driverDaoconst.save(d);
            return  DriverResponse.fromEntity(saveddriver);
        }else {
            return null;
        }
    }

    @Override
    public HashMap<String, String> deletedriver(Long id) {
        HashMap message=new HashMap<>();
        Driver d=driverDaoconst.findById(id).orElse(null);
        if (d !=null){
            try{
                driverDaoconst.delete(d);
                message.put("Etat","driver deleted successfully");

            }catch (Exception e){
                message.put("Etat",": "+e.getMessage());
            }
        }
        else{

            message.put("Etat","driver not found with this id: "+id);
        }
        return message;
    }
}

