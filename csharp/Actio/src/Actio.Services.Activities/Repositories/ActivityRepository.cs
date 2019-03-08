using System;
using System.Threading.Tasks;
using Actio.Services.Activities.Domain.Models;
using Actio.Services.Activities.Domain.Repositories;
using MongoDB.Driver;
using MongoDB.Driver.Linq;

namespace Actio.Services.Activities.Repositories
{
    public class ActivityRepository : IActivityRepository
    {
        private readonly IMongoDatabase _mongoDatabase;

        public ActivityRepository(IMongoDatabase mongoDatabase)
        {
            _mongoDatabase = mongoDatabase;
        }

        public async Task AddAsync(Activity activity)
        {
            await GetCollection().InsertOneAsync(activity);
        }

        public async Task<Activity> GetAsync(Guid id)
        {
            return await GetCollection().AsQueryable().FirstOrDefaultAsync(x=>x.Id == id);
        }

        private IMongoCollection<Activity> GetCollection()
        {
            return _mongoDatabase.GetCollection<Activity>("Activities");
        }
    }
}