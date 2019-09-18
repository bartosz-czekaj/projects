using Interview.Serializer;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Interview.Tests.Serializer
{
    [TestClass]
    public class SerializerTest
    {
        private ISerializer _serializer;

        [TestInitialize]
        public void Init()
        {
            _serializer = new SerializerImp(@"C:\Projects\C#\testEQ\Interview.Tests\Data\data.json");
        }

        [TestMethod]
        public void WhenDataIsSetThenDeserializingGivesNotNullValue()
        {
            var data =_serializer.Deserialize();

            Assert.IsNotNull(data);
        }

        [TestMethod]
        public void WhenFileNotExistsThenDeserializingGivesNullValue()
        {
            _serializer = new SerializerImp(@"C:\Projects\C#\testEQ\Interview.Tests\Data\nodata.json");

            var data = _serializer.Deserialize();

            Assert.IsNull(data);
        }
    }
}
