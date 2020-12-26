using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text.Json;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using PlanclockWebsite.Data;
using PlanclockWebsite.Models;

namespace PlanclockWebsite.Controllers
{
    public class RemindersController : Controller
    {
        private readonly PlanclockWebsiteContext _context;

        public static string apiRoot = "http://localhost:8083/reminders/";
        // GET: Reminders
        public async Task<IActionResult> Index()
        {
            List<Reminder> reminders = JavaWebApiLink.ReminderInterface.List();
            return View(reminders);
        }

        // GET: Reminders/Details/5
        public async Task<IActionResult> Details(int id)
        {
            if (id == null)
            {
                return NotFound();
            }

            string json = JavaWebApiLink.ReminderInterface.HttpGet("get/" +id);
            Reminder reminder = JsonSerializer.Deserialize<Reminder>(json);
            if (reminder == null)
            {
                return NotFound();
            }

            return View(reminder);
        }
    }
}
