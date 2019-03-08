using System;
using System.Linq;
using System.Threading.Tasks;
using MongoDB.Driver;

namespace Actio.Common.Mongo
{
    public class MongoSeeder : IDatabaseSeeder
    {
        protected readonly IMongoDatabase MongoDatabase;
        public MongoSeeder(IMongoDatabase mongoDatabase)
        {
            MongoDatabase = mongoDatabase;
        }
        public async Task SeedAsync()
        {
            var collectionCursor = await MongoDatabase.ListCollectionNamesAsync();
            var collections = await collectionCursor.ToListAsync();

            if(collections.Any())
            {
                return;
            }

            await CustomSeedAsync();

            
        }

        protected virtual async Task CustomSeedAsync()
        {
            await Task.CompletedTask;
        }
    }
}