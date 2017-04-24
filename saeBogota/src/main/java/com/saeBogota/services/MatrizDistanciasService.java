package com.saeBogota.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("MatrizDistanciasService")
@Transactional(readOnly = true)
public class MatrizDistanciasService {

    public boolean testDependencia(){
        return true;
    }
}
