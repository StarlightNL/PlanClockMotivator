using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using PlanclockWebsite.Models;

namespace PlanclockWebsite.Data
{
    public class PlanclockWebsiteContext : DbContext
    {
        public PlanclockWebsiteContext (DbContextOptions<PlanclockWebsiteContext> options)
            : base(options)
        {
        }

        public DbSet<PlanclockWebsite.Models.Reminder> Reminder { get; set; }

        public DbSet<PlanclockWebsite.Models.Motivation> Motivation { get; set; }
    }
}
