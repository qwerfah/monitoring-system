using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using StatisticsService.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace StatisticsService.Contexts
{
    public class StatisticsContext : DbContext
    {
        public DbSet<OperationRecord> OperationRecords { get; set; }

        public StatisticsContext(DbContextOptions options) : base(options)
        {
            Database.EnsureCreated();
        }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (optionsBuilder is null)
            {
                throw new ArgumentNullException(nameof(optionsBuilder));
            }

            optionsBuilder.EnableSensitiveDataLogging();
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            if (modelBuilder is null)
            {
                throw new ArgumentNullException(nameof(modelBuilder));
            }

            modelBuilder.ApplyConfiguration(new OperationRecordsConfiguration());
        }
    }

    public class OperationRecordsConfiguration : IEntityTypeConfiguration<OperationRecord>
    {
        public void Configure(EntityTypeBuilder<OperationRecord> builder)
        {
            if (builder is null)
            {
                throw new ArgumentNullException(nameof(builder));
            }

            builder.ToTable("OperationRecords");

            builder.HasAlternateKey(or => or.OperationRecordUid);

            builder.Property(or => or.OperationTime).IsRequired();
            builder.Property(or => or.ServiceName).IsRequired();
            builder.Property(or => or.MethodName).IsRequired();
            builder.Property(or => or.IsSuccessfull).IsRequired();
            builder.Property(or => or.StatusCode).IsRequired();
        }
    }
}
