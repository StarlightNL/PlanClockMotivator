using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text.Json;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using PlanclockWebsite.Data;
using PlanclockWebsite.Models;
using static PlanclockWebsite.Data.JavaWebApiLink;

namespace PlanclockWebsite.Controllers
{
    public class MotivationsController : Controller
    {
        private readonly PlanclockWebsiteContext _context;

        // GET: Motivations
        public async Task<IActionResult> Index()
        {
            try
            {
                List<Motivation> motivations = JavaWebApiLink.MotivationInterface.List();
                return View(motivations);
            }
            catch (Exception ex)
            {
                throw;
            }
        }

        // GET: Motivations/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            ViewBag.reminders = JavaWebApiLink.ReminderInterface.List();
            string json = JavaWebApiLink.MotivationInterface.HttpGet("get" + id);
            Motivation motivation = JsonSerializer.Deserialize<Motivation>(json);
            if (motivation == null)
            {
                return NotFound();
            }

            return View(motivation);
        }

        // GET: Motivations/Create
        public IActionResult Create()
        {
            List<Reminder> reminders = JavaWebApiLink.ReminderInterface.List();
            ViewBag.reminderOptions = reminders.Select(r =>
                                  new SelectListItem
                                  {
                                      Value = r.dbid.ToString(),
                                      Text = r.title
                                  }).ToList();
            return View();
        }

        // POST: Motivations/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("reminderId,motivationText")] Motivation motivation)
        {
            if (ModelState.IsValid)
            {
                JavaWebApiLink.MotivationInterface.Create(motivation);
                return RedirectToAction(nameof(Index));
            }
            return View(motivation);
        }

        // GET: Motivations/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            Motivation motivation = JavaWebApiLink.MotivationInterface.Get(id);
            if (motivation == null)
            {
                return NotFound();
            }
            return View(motivation);
        }

        // POST: Motivations/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("id,reminderId,motivationText")] Motivation motivation)
        {
            if (id != motivation.id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                JavaWebApiLink.MotivationInterface.Update(id, motivation);
                return RedirectToAction(nameof(Index));
            }
            return View(motivation);
        }

        // GET: Motivations/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            Motivation motivation = JavaWebApiLink.MotivationInterface.Get(id);

            if (motivation == null)
            {
                return NotFound();
            }

            return View(motivation);
        }

        // POST: Motivations/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            JavaWebApiLink.MotivationInterface.Delete(id);
            return RedirectToAction(nameof(Index));
        }

        private bool MotivationExists(int id)
        {
            Motivation motivation = JavaWebApiLink.MotivationInterface.Get(id);
            return motivation != null;
        }
    }
}