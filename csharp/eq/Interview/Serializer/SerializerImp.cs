using Interview.Models;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;

namespace Interview.Serializer
{
    public class SerializerImp : ISerializer
    {
        private static string _pathToData;

        public SerializerImp(string pathToData)
        {
            _pathToData = pathToData;
        }

        public IDictionary<Guid, Data> Deserialize()
        {
            return Loader();
        }

        public void Serialize()
        {
        }


        private static IDictionary<Guid, Data> Loader()
        {
            if (File.Exists(_pathToData))
            {
                var settings = new JsonSerializerSettings { NullValueHandling = NullValueHandling.Ignore, DateFormatHandling = DateFormatHandling.IsoDateFormat };

                String JSONtxt = File.ReadAllText(_pathToData);
                var data = JsonConvert.DeserializeObject<List<Data>>(JSONtxt, settings);

                return data.ToDictionary(x => x.Id);
            }

            return null;
        }
    }
}