using System;
using System.Threading.Tasks;
using Actio.Services.Identity.Domain.Models;
using Actio.Services.Identity.Domain.Repositories;
using MongoDB.Driver;
using MongoDB.Driver.Linq;

namespace Actio.Services.Identity.Repositories
{
    public class UserRepository : IUserRepository
    {

        private readonly IMongoDatabase _mongoDb;
        public UserRepository(IMongoDatabase mongoDb)
        {
            _mongoDb = mongoDb;
        }
        public async Task AddAsync(User user)
        {
            await GetCollection().InsertOneAsync(user);
        }

        public async Task<User> GetAsync(Guid id)
        {
            return await GetCollection().AsQueryable().FirstOrDefaultAsync(x=>x.Id == id);
        }

        public async Task<User> GetAsync(string email)
        {
            return await GetCollection().AsQueryable().FirstOrDefaultAsync(x=>x.Email.ToLowerInvariant() == email.ToLowerInvariant());
        }

        private IMongoCollection<User> GetCollection()
        {
            return _mongoDb.GetCollection<User>("Users");
        }
    }
}