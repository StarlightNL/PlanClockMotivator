using PlanclockWebsite.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text.Json;
using System.Threading.Tasks;

namespace PlanclockWebsite.Data
{
    public class MotivationLink : GetPostLink<Motivation>
    {
        public MotivationLink(HttpClient http, string apiurl) : base(http, apiurl + "motivations/")
        {
        }
    }
}
