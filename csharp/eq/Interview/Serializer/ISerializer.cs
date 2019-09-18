using Interview.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Interview.Serializer
{
    public interface ISerializer
    {
        IDictionary<Guid, Data> Deserialize();
        void Serialize();
    }
}
