using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Interview.Models
{
    [Serializable]
    public class Data
    {
        public Guid Id { get; set; }
        public Int32 ApplicationId { get; set; }
        public string Type { get; set; }
        public string Summary { get; set; }
        public Decimal Amount { get; set; }
        public DateTime PostingDate { get; set; }
        public bool IsCleared { get; set; }
        public DateTime ClearedDate { get; set; }
    }
}
 
 
 