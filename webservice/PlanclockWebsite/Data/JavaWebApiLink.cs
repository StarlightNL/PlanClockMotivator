using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

namespace PlanclockWebsite.Data
{
    public class JavaWebApiLink
    {
        private static string apiRoot = "http://localhost:8083/";
        public static ReminderLink ReminderInterface;
        public static MotivationLink MotivationInterface;
        private static HttpClient http = new HttpClient();

        static JavaWebApiLink(){
            http.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
            ReminderInterface = new ReminderLink(http, apiRoot);
            MotivationInterface = new MotivationLink(http, apiRoot);
        }
    }
}
