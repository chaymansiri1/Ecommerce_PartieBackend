package com.securityModel.controllers;


import com.securityModel.Dtos.request.DriverRequest;
import com.securityModel.Dtos.response.DriverResponse;
import com.securityModel.services.DriverService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/drivers")
public class DriverController {
    public final DriverService driverService;

    public DriverController(DriverService driverService) {

        this.driverService = driverService;
    }

    @PostMapping("/save")
    public DriverResponse adddriver(@RequestBody DriverRequest driverRequest){
        return driverService.createdriver(driverRequest);
    }

    @GetMapping("/all")
    public List<DriverResponse> alldriver(){
        return driverService.alldriver();
    }


    @GetMapping("/getbyid/{id}")
    public DriverResponse driverbyid(@PathVariable Long id){
        return driverService.driverById(id);
    }

    @PutMapping("/update/{id}")
    public DriverResponse updatedriver(@RequestBody DriverRequest driverRequest, @PathVariable Long id){
        return driverService.updatedriver(driverRequest,id);
    }
    @DeleteMapping("/delete/{id}")
    public HashMap<String, String> deletedriver(@PathVariable Long id){

        return driverService.deletedriver(id);
    }

}
