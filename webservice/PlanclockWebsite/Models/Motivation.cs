using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text.Json.Serialization;
using System.Threading.Tasks;

namespace PlanclockWebsite.Models
{
    public class Motivation
    {
        public int id { get; set; }
        public int reminderId { get; set; }
        [JsonPropertyName("reminderText")]
        [DisplayName("Reminder")]
        public String reminderTitle { get; set; }
        [DisplayName("Motivational quote")]
        public String motivationText { get; set; }
        [JsonPropertyName("createdate")]
        [DisplayName("Created at")]
        public DateTime createDate { get; set; }
        [JsonPropertyName("updatedate")]
        [DisplayName("Updated at")]
        public DateTime updateDate { get; set; }
    }
}
