package com.sketchandguess.api;

public class HuggingFaceAPICaller implements APICaller {
    private String apiToken;

//    public String call(String apiToken) {
//    }
//}
public HuggingFaceAPICaller(String apiToken) {
    this.apiToken = apiToken;
}

    @Override
    public String call(String prompt) {
        // TODO: 以后在这里写真正的 HTTP 调用

        // 现在只是为了能编译、能跑别的代码，先返回一个假的结果
        return "DUMMY_RESPONSE";
        // 也可以 return ""; 看你们 use case 期望什么
    }
}