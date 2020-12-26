using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace PlanclockWebsite.Models
{
    public class Reminder
    {
        [Key]
        public String id { get; set; }
        public int dbid { get; set; }
        public String title { get; set; }
        public DateTime plannedTime { get; set; }
        public bool done { get; set; }
        public List<Motivation> motivations { get; set; }
        public List<TimePlanning> timePlannings { get; set; }
        
    }
}
