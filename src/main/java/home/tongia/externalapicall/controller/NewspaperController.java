package home.tongia.externalapicall.controller;

import home.tongia.externalapicall.dto.Newspaper;
import home.tongia.externalapicall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/newspapers")
public class NewspaperController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Newspaper>> getNewspapers(){
        return new ResponseEntity<>(userService.getAllNewspapers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Newspaper> getNewspapersById(@PathVariable String id){
        return new ResponseEntity<>(userService.getNewspaperById(id), HttpStatus.OK);
    }

}
