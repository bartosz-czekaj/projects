using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Actio.Api.Models;
using MongoDB.Driver;
using MongoDB.Driver.Linq;

namespace Actio.Api.Repositories
{
    public class ActivityRepository : IActivityRepository
    {
        private readonly IMongoDatabase _mongoDatabase;

        public ActivityRepository(IMongoDatabase mongoDatabase)
        {
            _mongoDatabase = mongoDatabase;
        }

        public async Task AddAsync(Activity model)
        {
            await GetCollection().InsertOneAsync(model);
        }

        public async Task<IEnumerable<Activity>> BrowseAsync(Guid id)
        {
            return await GetCollection().AsQueryable().Where(x=>x.UserId == id).ToListAsync();
        }

        public async Task<Activity> GetAsync(Guid id)
        {
            return await GetCollection().AsQueryable().FirstOrDefaultAsync(x=>x.Id == id);;
        }

        private IMongoCollection<Activity> GetCollection()
        {
            return _mongoDatabase.GetCollection<Activity>("Activities");
        }
    }
}