package com.volumetricpixels.bans.spout.ingamegui;

import com.volumetricpixels.bans.shared.GUIPage;

public class VBSpoutAdminGUI {

    private final String admin;
    private GUIPage openPage = null;

    public VBSpoutAdminGUI(String admin) {
        this.admin = admin;
    }

    public GUIPage getOpenPage() {
        return openPage;
    }

    public void setOpenPage(GUIPage page) {
        this.openPage = page;
    }

    public String getAdmin() {
        return admin;
    }

}
