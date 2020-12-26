using PlanclockWebsite.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Security.Policy;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

namespace PlanclockWebsite.Data
{
    public class ReminderLink : GetPostLink<Reminder>
    {

        public ReminderLink(HttpClient http, string apiRoot) : base(http, apiRoot + "reminders/")
        {
        }


        public new List<Reminder> List()
        {
            string rJson = HttpGet("list");
            List<Reminder> reminders = JsonSerializer.Deserialize<List<Reminder>>(rJson);
            reminders = reminders.GroupBy(r => r.dbid).Select(g => g.First()).ToList();
            return reminders;
        }
    }
}
