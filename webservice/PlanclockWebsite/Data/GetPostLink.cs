using PlanclockWebsite.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

namespace PlanclockWebsite.Data
{
    public class GetPostLink<T>
    {
        protected HttpClient http;
        protected string apiurl;
        protected T model;
        public GetPostLink(HttpClient http, string apiurl)
        {
            this.http = http;
            this.apiurl = apiurl;
        }
        public String HttpGet(String url)
        {
            return http.GetStringAsync(apiurl + url).GetAwaiter().GetResult();
        }

        public string HttpPost(String url, Object data)
        {
            var content = new StringContent(JsonSerializer.Serialize(data), Encoding.UTF8, "application/json");
            var response = http.PostAsync(apiurl + url, content).GetAwaiter().GetResult();
            Console.WriteLine(123);
            return "";
        }

        public List<T> List()
        {
            string json = HttpGet("list");
            List<T> listItems = JsonSerializer.Deserialize<List<T>>(json);
            return listItems;
        }
        public T Get(object id)
        {
            string json = HttpGet("get/" + id.ToString());
            T item = JsonSerializer.Deserialize<T>(json);
            return item;
        }

        public void Delete(object id)
        {
            HttpGet("delete/" + id.ToString());
        }

        public T Create(Object data)
        {
            string response = HttpPost("create", data);
            return default(T);
        }

        public T Update(object id, Object data)
        {
            string response = HttpPost("update/"+id, data);
            return default(T);
        }
    }
}
