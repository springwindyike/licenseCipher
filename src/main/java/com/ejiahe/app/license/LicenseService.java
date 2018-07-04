package com.ejiahe.app.license;

import com.ejiahe.app.license.bean.CipherLicense;
import com.ejiahe.app.license.bean.License;
import com.ejiahe.app.license.manager.LicenseManager;

/**
 * Created by focus on 2018/7/4.
 */
public class LicenseService {

    LicenseManager lisenceManager = new LicenseManager();

    public CipherLicense createLisence(License license){
        return lisenceManager.createLisence(license);
    }
}
