module Maestri.MVC {
    requires javafx.controls;
    //requires com.google.gson;
    //requires ;
    exports Maestri.MVC;
    exports Maestri.MVC.Model.GModel;
    exports Maestri.MVC.Model.GModel.GamePlayer;
    exports Maestri.MVC.Model.GModel.MarbleMarket;
    exports Maestri.MVC.Model.GModel.GamePlayer.Playerboard;
    exports Maestri.MVC.Model.GModel.DevelopmentCards;
    exports Maestri.MVC.Model.GModel.LeaderCards;
    exports Communication.ClientSide;
    exports Communication.ServerSide;
    exports Message.MessageReceived;
    exports Message.MessageSent;
    //exports;
}
